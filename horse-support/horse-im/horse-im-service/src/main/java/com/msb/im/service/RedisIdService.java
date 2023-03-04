package com.msb.im.service;

import com.msb.framework.redis.RedisClient;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.msb.im.redis.RedisConstant.*;

/**
 * 依赖于redis的im id实现
 *
 * @author zhoumiao
 * @date 2022-04-16
 */
@Service
public class RedisIdService implements IIdService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private SessionUserService sessionUserService;

    @Override
    public Long generateMessageId() {
        return redisClient.increment(MESSAGE_ID_KEY);
    }

    @Override
    public Boolean existMessageId() {
        return redisClient.exists(MESSAGE_ID_KEY);
    }

    /**
     * 从数据库查询当前的最大id 即数据库记录数 更新缓存
     *
     * @return
     */
    @Override
    public void loadDbMaxMessageId2Cache() {
        RLock lock = redisClient.getLock(ADD_MESSAGE_ID_LOCK);
        try {
            lock.lock(1, TimeUnit.MINUTES);
            if (existMessageId()) {
                return;
            }
            Long dbMaxId = messageService.findDbMaxId();
            if (dbMaxId == null) {
                dbMaxId = 0L;
            }
            updateMessageId(dbMaxId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateMessageId(Long maxId) {
        redisClient.set(MESSAGE_ID_KEY, maxId);
    }

    @Override
    public Long generateSessionId() {
        return redisClient.increment(SESSION_ID_KEY);
    }

    @Override
    public Boolean existSessionId() {
        return redisClient.exists(SESSION_ID_KEY);
    }

    /**
     * 从数据库查询当前的最大id 即数据库记录数 更新缓存
     *
     * @return
     */
    @Override
    public void loadDbMaxSessionId2Cache() {
        RLock lock = redisClient.getLock(ADD_SESSION_ID_LOCK);
        try {
            lock.lock(1, TimeUnit.MINUTES);
            if (existSessionId()) {
                return;
            }
            Long dbMaxId = sessionService.findDbMaxId();
            if (dbMaxId == null) {
                dbMaxId = 0L;
            }
            updateSessionId(dbMaxId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateSessionId(Long maxId) {
        redisClient.set(SESSION_ID_KEY, maxId);
    }

    @Override
    public Long generateSessionUserId() {
        return redisClient.increment(SESSION_USER_ID_KEY);
    }

    @Override
    public Boolean existSessionUserId() {
        return redisClient.exists(SESSION_USER_ID_KEY);
    }

    /**
     * 从数据库查询当前的最大id 即数据库记录数 更新缓存
     *
     * @return
     */
    @Override
    public void loadDbMaxSessionUserId2Cache() {
        RLock lock = redisClient.getLock(ADD_SESSION_USER_ID_LOCK);
        try {
            lock.lock(1, TimeUnit.MINUTES);
            if (existSessionUserId()) {
                return;
            }
            Long dbMaxId = sessionUserService.findDbMaxId();
            if (dbMaxId == null) {
                dbMaxId = 0L;
            }
            updateSessionUserId(dbMaxId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateSessionUserId(Long maxId) {
        redisClient.set(SESSION_USER_ID_KEY, maxId);
    }

    @Override
    public Long generateSessionMessageId(Long sessionId) {
        String sessionMessageIdKey = getSessionMessageIdKey(sessionId);
        if (redisClient.exists(sessionMessageIdKey)) {
            return redisClient.increment(sessionMessageIdKey);
        }
        // 加载会话中消息id
        String sessionMessageIdLockKey = getSessionMessageIdLockKey(sessionId);
        RLock lock = redisClient.getLock(sessionMessageIdLockKey);
        try {
            lock.lock(1, TimeUnit.MINUTES);
            if (!redisClient.exists(sessionMessageIdKey)) { // 双层空判断提高性能
                // 从数据库查询会话中最大的消息id
                messageService.findDbMessageIndexMaxIdAndUpdateRedis(sessionId);
            }
            return redisClient.increment(sessionMessageIdKey);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateSessionMessageMaxId(Long sessionId, Long sessionMessageMaxId) {
        String sessionMessageIdKey = getSessionMessageIdKey(sessionId);
        redisClient.set(sessionMessageIdKey, sessionMessageMaxId, MESSAGE_INDEX_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    private String getSessionMessageIdLockKey(Long sessionId) {
        return format(MESSAGE_INDEX_LOCK, sessionId);
    }

    private String getSessionMessageIdKey(Long sessionId) {
        return format(MESSAGE_INDEX_KEY, sessionId);
    }
}
