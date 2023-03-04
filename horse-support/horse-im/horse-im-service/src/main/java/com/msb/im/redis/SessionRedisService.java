package com.msb.im.redis;

import com.msb.framework.redis.RedisClient;
import com.msb.im.model.bo.SessionUserBO;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.msb.im.redis.RedisConstant.*;

/**
 * 会话相关的redis操作服务
 * 缓存类型
 *
 * @author zhoumiao
 * @date 2022-04-20
 */
@Service
@Slf4j
public class SessionRedisService {
    public static final int USER_SESSION_CACHE_SIZE = 100;

    @Resource
    private RedisClient redisClient;
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionUserService sessionUserService;

    /**
     * 查询用户会话id
     *
     * @param userId
     * @param sysId
     * @return
     */
    public List<Long> getUserSessionIds(String userId, Integer sysId) {
        String userSessionZsetKey = getUserSessionZsetKey(sysId, userId);
        LinkedHashSet<Object> sessionIds = redisClient.zAll(userSessionZsetKey);
        if (sessionIds == null) {
            return Collections.emptyList();
        } else {
            List<Long> sessionIdList = new LinkedList<>();
            for (Object sessionId : sessionIds) {
                sessionIdList.add(Long.parseLong(sessionId.toString()));
            }
            return sessionIdList;
        }
    }


    /**
     * 查询会话
     *
     * @param sessionId
     * @return
     */
    public Session getSession(Long sessionId) {
        String sessionKey = getSessionKey(sessionId);
        return redisClient.get(sessionKey);
    }

    /**
     * 批量查询会话
     *
     * @param sessionIds
     * @return
     */
    public List<Session> getSessions(List<Long> sessionIds) {
        if (sessionIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Session> redisSessions = getSessionList(sessionIds);

        Set<Long> redisSessionIds = redisSessions.stream().map(Session::getId).collect(Collectors.toSet());
        List<Long> selectDbSessionIds = new ArrayList<>(sessionIds);
        if (redisSessions.isEmpty() || (selectDbSessionIds.removeAll(redisSessionIds) && !selectDbSessionIds.isEmpty())) {
            redisSessions.addAll(sessionService.findDbByIdsAndUpdateRedis(selectDbSessionIds));
        }

        return sortSession(sessionIds, redisSessions);
    }

    private List<Session> getSessionList(List<Long> sessionIds) {
        List<Session> sessions = new ArrayList<>(sessionIds.size());
        for (Long sessionId : sessionIds) {
            Session session = getSession(sessionId);
            if (session != null) {
                sessions.add(session);
            }
        }
        return sessions;
    }

    private List<Session> sortSession(List<Long> sessionIds, List<Session> sessions) {
        List<Session> sortSessions = new ArrayList<>(sessions.size());
        Map<Long, Session> sessionMap = sessions.stream().collect(Collectors.toMap(Session::getId, Function.identity()));
        for (Long sessionId : sessionIds) {
            sortSessions.add(sessionMap.get(sessionId));
        }
        return sortSessions;
    }

    /**
     * 添加会话
     *
     * @param session
     */
    public void addSession(Session session) {
        String sessionKey = getSessionKey(session.getId());
        redisClient.set(sessionKey, session, SESSION_EXPIRED_DAYS, TimeUnit.DAYS);
    }

    /**
     * 更新用户会话未读数
     *
     * @param userId
     * @param sessionId
     * @param unreadCount
     * @return
     */
    public void updateSessionUserUnreadCount(String userId, Long sessionId, Long unreadCount) {
        String userSessionUnreadKey = getUserSessionUnreadKey(userId, sessionId);
        redisClient.set(userSessionUnreadKey, unreadCount, USER_SESSION_UNREAD_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    /**
     * 新增用户会话未读数 缓存不存在时会从数据库加载数据到缓存
     *
     * @param userId
     * @param sessionId
     * @param updateTime
     * @return
     */
    public void incrementSessionUserUnreadCount(String userId, Long sessionId, Long updateTime) {
        String userSessionUnreadKey = getUserSessionUnreadKey(userId, sessionId);
        if (!redisClient.exists(userSessionUnreadKey)) {
            sessionUserService.loadDbUserSessionUnread2Redis(userId, sessionId, updateTime, 1);
        } else {
            redisClient.increment(userSessionUnreadKey, 1);
            redisClient.expire(userSessionUnreadKey, USER_SESSION_UNREAD_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    public boolean existSessionUserUnreadCount(String userId, Long sessionId) {
        String userSessionUnreadKey = getUserSessionUnreadKey(userId, sessionId);
        return redisClient.exists(userSessionUnreadKey);
    }

    public RLock getAddUserSessionUnreadLock(String userId, Long sessionId) {
        String userSessionUnreadKey = getAddUserSessionUnreadLockKey(userId, sessionId);
        return redisClient.getLock(userSessionUnreadKey);
    }

    /**
     * 查询用户会话未读数
     *
     * @param userId
     * @param sessionId
     * @return
     */
    public Long getSessionUserUnreadCount(String userId, Long sessionId) {
        String userSessionUnreadKey = getUserSessionUnreadKey(userId, sessionId);
        Object o = redisClient.get(userSessionUnreadKey);
        if (o == null) {
            return null;
        } else {
            return Long.parseLong(o.toString());
        }
    }

    /**
     * 清空会话未读消息数
     *
     * @param sessionId
     * @param userId
     */
    public void readMessage(Long sessionId, String userId) {
        String userSessionUnreadKey = getUserSessionUnreadKey(userId, sessionId);
        redisClient.set(userSessionUnreadKey, 0, USER_SESSION_UNREAD_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    /**
     * 查询会话关联的用户id
     *
     * @param sessionId
     * @return
     */
    public Set<String> getSessionUserIds(Long sessionId) {
        String sessionUsersSetKey = getSessionUserIdsSetKey(sessionId);
        Set<Object> objects = redisClient.sMembers(sessionUsersSetKey);
        if (objects == null) {
            return Collections.emptySet();
        }
        Set<String> set = new HashSet<>();
        for (Object object : objects) {
            set.add(String.valueOf(object.toString()));
        }
        return set;
    }

    /**
     * 新增会话关联的用户id
     *
     * @param sessionId
     * @param userIds
     */
    public void addSessionUserIds(Long sessionId, Set<String> userIds) {
        if (userIds.isEmpty()) {
            return;
        }
        String sessionUsersSetKey = getSessionUserIdsSetKey(sessionId);
        if (!redisClient.exists(sessionUsersSetKey)) {
            sessionUserService.loadDbSessionUserIds2Redis(sessionId, userIds);
        } else {
            redisClient.sAdd(sessionUsersSetKey, userIds.toArray());
            redisClient.expire(sessionUsersSetKey, SESSION_USERS_ID_EXPIRED_DAYS, TimeUnit.DAYS);
        }
    }

    /**
     * 更新会话关联的用户id
     *
     * @param sessionId
     * @param userIds
     */
    public void updateSessionUserIds(Long sessionId, Set<String> userIds) {
        if (userIds.isEmpty()) {
            return;
        }
        String sessionUsersSetKey = getSessionUserIdsSetKey(sessionId);
        redisClient.sAdd(sessionUsersSetKey, userIds.toArray());
        redisClient.expire(sessionUsersSetKey, SESSION_USERS_ID_EXPIRED_DAYS, TimeUnit.DAYS);
    }


    /**
     * 是否存在会话对应的用户id缓存存在
     *
     * @param sessionId
     * @return
     */
    public boolean existSessionUserIdsSet(Long sessionId) {
        String sessionUsersSetKey = getSessionUserIdsSetKey(sessionId);
        return redisClient.exists(sessionUsersSetKey);
    }


    public RLock getAddSessionUsersSetLock(Long sessionId) {
        String sessionUsersSetKeyLock = getSessionUsersSetKeyLock(sessionId);
        return redisClient.getLock(sessionUsersSetKeyLock);
    }

    /**
     * 更新用户会话id顺序
     *
     * @param sessionId
     * @param updateTime
     * @param userIds
     * @param sysId
     */
    public void updateUserSessionIdSort(Long sessionId, Long updateTime, Set<String> userIds, Integer sysId) {
        long score = getScore(updateTime);
        for (String userId : userIds) {
            String userSessionZsetKey = getUserSessionZsetKey(sysId, userId);
            if (!redisClient.exists(userSessionZsetKey)) { // 缓存不存在时 先更新缓存 在插入缓存
                sessionService.loadDbUserSessionId2Redis(userId, sysId, null, sessionId, updateTime);
            }
            if (redisClient.exists(userSessionZsetKey)) { //
                redisClient.zAdd(userSessionZsetKey, sessionId, score);
                redisClient.expire(userSessionZsetKey, USER_SESSION_ZSET_EXPIRED_DAYS, TimeUnit.DAYS);
            }
        }
    }

    /**
     * 设置用户会话id
     *
     * @param sessionUserBOS
     * @return
     */
    public void addUserSessionIds(String userId, Integer sysId, List<SessionUserBO> sessionUserBOS) {
        String userSessionZsetKey = getUserSessionZsetKey(sysId, userId);
        Consumer<RedisOperations<String, Object>> addUserSessionIdsConsumer = getRedisAddUserSessionIdsConsumer(sessionUserBOS, userSessionZsetKey);
        redisClient.pipelined(addUserSessionIdsConsumer);
        // 删除多余的会话 只保留指定个数的会话
        Long size = redisClient.zSize(userSessionZsetKey);
        if (size > USER_SESSION_CACHE_SIZE) {
            redisClient.zRemoveRange(userSessionZsetKey, USER_SESSION_CACHE_SIZE, size);
        }
        redisClient.expire(userSessionZsetKey, USER_SESSION_ZSET_EXPIRED_DAYS, TimeUnit.DAYS);
    }

    private Consumer<RedisOperations<String, Object>> getRedisAddUserSessionIdsConsumer(List<SessionUserBO> sessionUserBOS, String userSessionZsetKey) {
        Consumer<RedisOperations<String, Object>> consumer = redisOperations -> {
            for (SessionUserBO sessionUserBO : sessionUserBOS) {
                long score = getScore(sessionUserBO.getUpdateTimeStamp());
                redisOperations.opsForZSet().add(userSessionZsetKey, sessionUserBO.getSessionId(), score);
            }
        };
        return consumer;
    }

    public RLock getAddUserSessionIdsZsetLock(String userId, Integer sysId) {
        String userSessionZsetKeyLock = getUserSessionZsetKeyLock(sysId, userId);
        return redisClient.getLock(userSessionZsetKeyLock);
    }

    private long getScore(Long timeStamp) {
        // 公元3000年时间戳
        long timeStamp3000 = 32600000000000L;
        // 更新时间是越近的更新时间戳越大，放到zset中顺序就会往后；要满足越近的更新越往前的顺序，用更新时间距离3000年的时间差作为分数就可以满足我们的使用场景
        return timeStamp3000 - timeStamp;
    }

    public void updateUpdateTime(Long sessionId, Long updateTime, String updateUser) {
        Session redisSession = getSession(sessionId);
        if (redisSession != null) {
            redisSession.setUpdateTimeStamp(updateTime);
            redisSession.setUpdateUser(updateUser);
            addSession(redisSession);
        }
    }

    public SessionUser getSessionUser(Long sessionId, String userId) {
        String sessionUserStringKey = getSessionUserStringKey(userId, sessionId);
        return redisClient.get(sessionUserStringKey);
    }

    public void addSessionUser(SessionUser sessionUser) {
        if (sessionUser == null) {
            return;
        }
        String sessionUserStringKey = getSessionUserStringKey(sessionUser.getUserId(), sessionUser.getSessionId());
        redisClient.set(sessionUserStringKey, sessionUser, SESSION_USER_EXPIRED_DAYS, TimeUnit.DAYS);
    }

    public void removeSessionUser(String userId, Long sessionId) {
        String sessionUserStringKey = getSessionUserStringKey(userId, sessionId);
        redisClient.delete(sessionUserStringKey);
    }

    public void removeSession(Long sessionId) {
        String sessionUserStringKey = getSessionKey(sessionId);
        redisClient.delete(sessionUserStringKey);
    }

    /**
     * 删除用户的会话
     *  @param userId
     * @param sessionId
     * @param systemId
     */
    public void removeUserSessionId(String userId, Long sessionId, Integer systemId) {
        String userSessionZsetKey = getUserSessionZsetKey(systemId, userId);
        redisClient.zRemove(userSessionZsetKey, sessionId);
    }

    private String getUserSessionZsetKey(Integer sysId, String userId) {
        return RedisConstant.format(RedisConstant.USER_SESSION_ID_ZSET, sysId, userId);
    }

    private String getUserSessionZsetKeyLock(Integer sysId, String userId) {
        return RedisConstant.format(RedisConstant.ADD_USER_SESSION_ID_ZSET_LOCK, sysId, userId);
    }

    private String getSessionUserIdsSetKey(Long sessionId) {
        return RedisConstant.format(RedisConstant.SESSION_USER_ID_SET, sessionId);
    }

    private String getSessionUsersSetKeyLock(Long sessionId) {
        return RedisConstant.format(RedisConstant.ADD_SESSION_USER_ID_SET_LOCK, sessionId);
    }

    private String getSessionKey(Long sessionId) {
        return RedisConstant.format(RedisConstant.SESSION, sessionId);
    }

    private String getUserSessionUnreadKey(String userId, Long sessionId) {
        return RedisConstant.format(RedisConstant.USER_SESSION_UNREAD, userId, sessionId);
    }

    private String getAddUserSessionUnreadLockKey(String userId, Long sessionId) {
        return RedisConstant.format(RedisConstant.ADD_USER_SESSION_UNREAD_LOCK, userId, sessionId);
    }

    private String getUserSessionUnreadMapKey(String userId, Long sessionId) {
        return RedisConstant.format(RedisConstant.USER_SESSION_UNREAD_MAP, userId, sessionId);
    }

    private String getSessionUserStringKey(String userId, Long sessionId) {
        return RedisConstant.format(RedisConstant.SESSION_USER, sessionId, userId);
    }

}
