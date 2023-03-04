package com.msb.im.mq.service;

import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.module.waiter.service.StoreConfigService;
import com.msb.im.module.waiter.service.StoreWaiterService;
import com.msb.im.module.waiter.service.UserWaiterRelationService;
import com.msb.im.mq.model.ImMqMessage;
import com.msb.im.redis.MessageRedisService;
import com.msb.im.redis.SessionRedisService;
import com.msb.im.service.MessageService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhou miao
 * @date 2022/05/06
 */
@Service
@Slf4j
public class SendAndUpdateSessionImMqMessageHandleService extends AbstractImMqMessageHandleService {
    @Resource
    private MessageService messageService;
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private SessionRedisService sessionRedisService;
    @Resource
    private MessageRedisService messageRedisService;
    @Resource
    private StoreWaiterService storeWaiterService;
    @Resource
    private StoreConfigService storeConfigService;
    @Resource
    private UserWaiterRelationService userWaiterRelationService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handle2Db(ImMqMessage imMqMessage) {
        // 更新会话
        Session session = imMqMessage.getSession();
        sessionService.updateById(session);

        // 客服会话需要单独处理
        boolean isStoreSession = sessionService.isStoreSession(session.getId());
        List<StoreWaiter> storeWaiters = null;
        StoreConfig storeConfig = null;
        if (isStoreSession) {
            storeWaiters = storeWaiterService.findBySysId(session.getSysId());
            storeConfig = storeConfigService.findBySysId(session.getSysId());
        }

        // 更新会话用户关联
        List<SessionUser> sessionUsers = imMqMessage.getSessionUsers();
        String fromId = imMqMessage.getMessage().getFromId();
        for (SessionUser sessionUser : sessionUsers) {

            // 会话接收人未读加1
            String sessionUserId = sessionUser.getUserId();
            boolean isAddUnread;
            if (isStoreSession) {
                isAddUnread = storeSessionIsAddUnread(storeWaiters, storeConfig, fromId, sessionUserId);
            } else {
                isAddUnread = !Objects.equals(fromId, sessionUserId);
            }

            SessionUser update = getUpdateEntity(sessionUser);
            SessionUser updateCondition = getUpdateCondition(sessionUser);
            sessionUserService.updateByCondition(update, updateCondition, isAddUnread);
        }

        // 新增消息
        Message message = imMqMessage.getMessage();
        messageService.save(message);
    }

    private boolean storeSessionIsAddUnread(List<StoreWaiter> storeWaiters, StoreConfig storeConfig, String fromId, String sessionUserId) {
        boolean isAddUnread;
        Long storeId = storeConfig.getId();
        if (fromIsWaiter(storeWaiters, fromId)) {
            isAddUnread = !Objects.equals(sessionUserId, storeId.toString());
        } else if (fromIsStore(fromId)) {
            isAddUnread = true;
        } else {
            isAddUnread = Objects.equals(sessionUserId, storeId.toString());
        }
        return isAddUnread;
    }

    private boolean fromIsStore(String fromId) {
        return Objects.isNull(fromId);
    }

    private boolean fromIsWaiter(List<StoreWaiter> storeWaiters, String fromId) {
        return storeWaiters.stream().anyMatch(e -> Objects.equals(e.getWaiterId(), fromId));
    }

    private SessionUser getUpdateEntity(SessionUser sessionUser) {
        return SessionUser.builder()
                .updateTimeStamp(sessionUser.getUpdateTimeStamp())
                .updateUser(sessionUser.getUpdateUser())
                .isDeleted(sessionUser.getIsDeleted())
                .userAvatar(sessionUser.getUserAvatar())
                .userNickname(sessionUser.getUserNickname())
                .build();
    }

    private SessionUser getUpdateCondition(SessionUser sessionUser) {
        return SessionUser.builder()
                .sessionId(sessionUser.getSessionId())
                .userId(sessionUser.getUserId())
                .build();
    }

    @Override
    public void handle2Cache(ImMqMessage imMqMessage) {
        // 更新会话更新时间
        Session session = imMqMessage.getSession();
        Long sessionId = session.getId();
        Long updateTime = imMqMessage.getMessage().getCreateTimeStamp();
        sessionRedisService.updateUpdateTime(session.getId(), session.getUpdateTimeStamp(), session.getUpdateUser());

        // 客服会话需要单独处理
        boolean isStoreSession = sessionService.isStoreSession(session.getId());
        List<StoreWaiter> storeWaiters = null;
        StoreConfig storeConfig = null;
        if (isStoreSession) {
            storeWaiters = storeWaiterService.findBySysId(session.getSysId());
            storeConfig = storeConfigService.findBySysId(session.getSysId());
        }

        // 更新用户未读 用户会话顺序
        List<SessionUser> sessionUsers = imMqMessage.getSessionUsers();
        Set<String> userIds = new HashSet<>();
        boolean isAddUnread;
        for (SessionUser sessionUser : sessionUsers) {
            String sessionUserId = sessionUser.getUserId();
            userIds.add(sessionUserId);
            if (isStoreSession) {
                isAddUnread = storeSessionIsAddUnread(storeWaiters, storeConfig, imMqMessage.getMessage().getFromId(), sessionUserId);
            } else {
                isAddUnread = !Objects.equals(imMqMessage.getMessage().getFromId(), sessionUserId);
            }
            if (isAddUnread) {
                sessionRedisService.incrementSessionUserUnreadCount(sessionUserId, sessionId, updateTime);
            }
        }

        // 用户会话顺序缓存
        sessionRedisService.updateUserSessionIdSort(sessionId, updateTime, userIds, imMqMessage.getSession().getSysId());

        // 新增消息缓存 新增会话消息缓存 新增会话最后一条消息
        Message message = imMqMessage.getMessage();
        messageRedisService.addMessage(message);
        messageRedisService.addSessionMessageId(message.getId(), message.getSessionId());
    }
}
