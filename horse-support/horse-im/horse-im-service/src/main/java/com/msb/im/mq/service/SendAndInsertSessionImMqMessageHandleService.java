package com.msb.im.mq.service;

import com.msb.framework.redis.RedisClient;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.mq.model.ImMqMessage;
import com.msb.im.redis.MessageRedisService;
import com.msb.im.redis.RedisConstant;
import com.msb.im.redis.SessionRedisService;
import com.msb.im.service.MessageService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import com.msb.im.util.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.msb.im.redis.RedisConstant.IM_CONSUME_SEND_MESSAGE_LOCK;

/**
 * @author zhou miao
 * @date 2022/05/06
 */
@Service
@Slf4j
public class SendAndInsertSessionImMqMessageHandleService extends AbstractImMqMessageHandleService {
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
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    @Resource
    private RedisClient redisClient;

    public String getSendMessageLockKey(Integer sysId, String fromId, String toId) {
        long fromMd5 = DigestUtil.md52Long(fromId);
        long toMd5 = DigestUtil.md52Long(toId);
        return RedisConstant.format(IM_CONSUME_SEND_MESSAGE_LOCK, sysId, fromMd5 + toMd5);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handle2Db(ImMqMessage imMqMessage) {
        Session session = imMqMessage.getSession();
        Message message = imMqMessage.getMessage();
        if (Objects.equals(session.getType(), SessionTypeEnum.GROUP)) {

        } else {
            String toUserId = imMqMessage.getSessionUsers().stream()
                    .filter(sessionUser -> Objects.equals(sessionUser.getUserId(), message.getFromId()))
                    .map(SessionUser::getRelationUserId)
                    .findFirst().orElse(null);
            if (toUserId == null) {
                log.error("消费mq发送消息新增会话的参数错误 {}", imMqMessage);
                return;
            }

            // 防止多条消息并发发送会产生多条相同会话
            String sendMessageLockKey = getSendMessageLockKey(session.getSysId(), message.getFromId(), toUserId);
            RLock lock = redisClient.getLock(sendMessageLockKey);
            try {
                lock.lock(1, TimeUnit.MINUTES);
                // 新增或者更新会话
                sessionService.save(session);

                // 新增或者更新会话用户关联
                List<SessionUser> sessionUsers = imMqMessage.getSessionUsers();
                for (SessionUser sessionUser : sessionUsers) {
                    sessionUserService.save(sessionUser);
                }
            } finally {
                lock.unlock();
            }

            // 新增消息
            messageService.save(message);
        }
    }

    @Override
    public void handle2Cache(ImMqMessage imMqMessage) {
        // 新增会话缓存
        Session session = imMqMessage.getSession();
        Long sessionId = session.getId();
        Long updateTime = imMqMessage.getMessage().getCreateTimeStamp();
        sessionRedisService.addSession(session);

        // 新增用户未读、会话中包含的用户、用户会话顺序
        List<SessionUser> sessionUsers = imMqMessage.getSessionUsers();
        Set<String> userIds = new HashSet<>();
        for (SessionUser sessionUser : sessionUsers) {
            String userId = sessionUser.getUserId();
            userIds.add(userId);
            sessionRedisService.addSessionUser(sessionUser);
            if (!Objects.equals(imMqMessage.getMessage().getFromId(), userId)) {
                sessionRedisService.incrementSessionUserUnreadCount(userId, sessionId, updateTime);
            }
        }
        // 用户会话顺序缓存 会话中用户的缓存
        sessionRedisService.updateUserSessionIdSort(sessionId, updateTime, userIds, imMqMessage.getSession().getSysId());
        sessionRedisService.addSessionUserIds(sessionId, userIds);

        // 新增消息缓存 新增会话消息缓存 新增会话最后一条消息
        Message message = imMqMessage.getMessage();
        messageRedisService.addMessage(message);
        messageRedisService.addSessionMessageId(message.getId(), message.getSessionId());
    }

}
