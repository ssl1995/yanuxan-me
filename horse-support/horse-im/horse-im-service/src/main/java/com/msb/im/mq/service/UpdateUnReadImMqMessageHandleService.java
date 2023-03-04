package com.msb.im.mq.service;

import com.google.common.collect.Lists;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.mq.model.*;
import com.msb.im.redis.SessionRedisService;
import com.msb.im.service.SessionUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: zhou miao
 * @create: 2022/05/06
 */
@Service
public class UpdateUnReadImMqMessageHandleService extends AbstractImMqMessageHandleService {
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private SessionRedisService sessionRedisService;

    /**
     * 创建更新消息已读的mq消息数据
     *
     * @param sessionId
     * @param userId
     * @return
     */
    public ImMqMessage createUpdateUnReadImMqMessage(Long sessionId, String userId) {
        ImMqMessage imMqMessage = new ImMqMessage();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUpdateUser(userId);
        sessionUser.setUpdateTimeStamp(System.currentTimeMillis());
        sessionUser.setUnReadCount(0L);
        sessionUser.setUserId(userId);
        sessionUser.setSessionId(sessionId);
        imMqMessage.setImMqMessageTypeEnum(ImMqMessageTypeEnum.UPDATE_UNREAD);
        imMqMessage.setUuid(UUID.randomUUID().toString());
        imMqMessage.setSessionUsers(Lists.newArrayList(sessionUser));
        return imMqMessage;
    }

    @Override
    public void handle2Db(ImMqMessage imMqMessage) {
        List<SessionUser> sessionUsers = imMqMessage.getSessionUsers();
        for (SessionUser sessionUser : sessionUsers) {
            sessionUserService.updateDbUnreadCount(sessionUser.getSessionId(), sessionUser.getUserId(), sessionUser.getUnReadCount(), sessionUser.getUpdateTimeStamp(), sessionUser.getUpdateUser());
        }
    }

    @Override
    public void handle2Cache(ImMqMessage imMqMessage) {
        List<SessionUser> sessionUsers = imMqMessage.getSessionUsers();
        for (SessionUser sessionUser : sessionUsers) {
            sessionRedisService.readMessage(sessionUser.getSessionId(), sessionUser.getUserId());
        }
    }
}
