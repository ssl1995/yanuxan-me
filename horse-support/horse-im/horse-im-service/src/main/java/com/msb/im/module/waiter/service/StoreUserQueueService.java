package com.msb.im.module.waiter.service;

import com.msb.framework.redis.RedisClient;
import com.msb.im.module.waiter.model.channelmessage.StoreUserSendMessage;
import com.msb.im.module.waiter.model.entity.UserWaiterRelation;
import com.msb.im.redis.RedisConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

import static com.msb.im.redis.RedisConstant.STORE_RECEIVE_USER_LIST_KEY;

/**
 * 客服会话用户待处理队列服务
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Service
public class StoreUserQueueService {
    // 之前未分配客服的用户所进入的队列
    private final static String ALL_USER_QUEUE_ID = "-11";

    // 消费自己的队列的个数
    private final static int CONSUME_SIZE = 10;

    @Resource
    private RedisClient redisClient;

    public void pushUser(StoreUserSendMessage storeUserSendMessage, String waiterId) {
        Long storeId = storeUserSendMessage.getStoreId();
        String fromId = storeUserSendMessage.getFromId();
        UserWaiterRelation userWaiterRelation = new UserWaiterRelation();
        userWaiterRelation.setStoreId(storeId);
        userWaiterRelation.setUserId(fromId);
        userWaiterRelation.setSessionId(storeUserSendMessage.getToSessionId());
        waiterId = waiterId == null ? ALL_USER_QUEUE_ID : waiterId;
        String storeReceiveUserListKey = getStoreReceiveUserListKey(storeUserSendMessage.getSysId(), storeId, waiterId);

        // 自己队列只保留10个 多余的放到公共队列
        if (redisClient.lSize(storeReceiveUserListKey) < CONSUME_SIZE) {
            redisClient.lRemove(storeReceiveUserListKey, userWaiterRelation);
            redisClient.lPush(storeReceiveUserListKey, userWaiterRelation);
        } else {
            String publicStoreReceiveUserListKey = getStoreReceiveUserListKey(storeUserSendMessage.getSysId(), storeId, ALL_USER_QUEUE_ID);
            redisClient.lRemove(publicStoreReceiveUserListKey, userWaiterRelation);
            redisClient.lPush(publicStoreReceiveUserListKey, userWaiterRelation);
        }
    }

    public List<UserWaiterRelation> pullUser(Integer systemId, Long storeId, String waiterId, int size) {
        String storeReceiveUserListKey = getStoreReceiveUserListKey(systemId, storeId, waiterId);
        List<UserWaiterRelation> userWaiterRelations = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            UserWaiterRelation userWaiterRelation = redisClient.lPop(storeReceiveUserListKey);
            if (userWaiterRelation != null) {
                userWaiterRelations.add(userWaiterRelation);
            }
        }
        return userWaiterRelations;
    }

    private String getStoreReceiveUserListKey(Integer sysId, Long storeId, String waiterId) {
        return RedisConstant.format(STORE_RECEIVE_USER_LIST_KEY, sysId, storeId, waiterId);
    }


    public List<UserWaiterRelation> allocateUser(Integer systemId, Long storeId, String waiterId) {
        List<UserWaiterRelation> userWaiterRelations = pullUser(systemId, storeId, waiterId, CONSUME_SIZE);
        if (userWaiterRelations.size() < CONSUME_SIZE) {
            userWaiterRelations.addAll(pullUser(systemId, storeId, ALL_USER_QUEUE_ID, CONSUME_SIZE - userWaiterRelations.size()));
        }
        return userWaiterRelations;
    }

}
