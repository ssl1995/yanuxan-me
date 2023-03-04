package com.msb.im.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.mapper.SessionMapper;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.model.bo.SessionUserBO;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.module.chat.model.CreateSessionMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.service.StoreConfigService;
import com.msb.im.netty.model.HandshakeParam;
import com.msb.im.netty.service.UserConnectService;
import com.msb.im.redis.SessionRedisService;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.msb.im.redis.SessionRedisService.USER_SESSION_CACHE_SIZE;

/**
 * (HorseImSession)表服务实现类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:19
 */
@Service("sessionService")
public class SessionService extends ServiceImpl<SessionMapper, Session> {
    @Resource
    private SessionRedisService sessionRedisService;
    @Resource
    private IIdService iIdService;
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private StoreConfigService storeConfigService;
    @Resource
    private UserConnectService userConnectService;

    public Boolean delete(List<Long> idList) {
        return this.removeByIds(idList);
    }

    public Session generateMqSession(SendMessageBO sendMessageBO, Long nowTimestamp) {
        Long sessionId = iIdService.generateSessionId();
        Session session = new Session();
        session.setId(sessionId);
        session.setSysId(sendMessageBO.getSysId());
        session.setType(sendMessageBO.getSessionTypeEnum().getCode());
        session.setCreateTimeStamp(nowTimestamp);
        session.setUpdateTimeStamp(nowTimestamp);
        session.setCreateUser(sendMessageBO.getFromId());
        session.setUpdateUser(sendMessageBO.getFromId());
        session.setIsDeleted(false);
        session.setPayload(sendMessageBO.getSessionPayload());
        return session;
    }

    /**
     * 从数据库查询当前最大id 即数据库记录数 更新缓存
     *
     * @return
     */
    public Long findDbMaxId() {
        return baseMapper.findMaxId();
    }


    /**
     * 是否客服会话
     *
     * @param sessionId
     * @return 否客服会话
     */
    public boolean isStoreSession(Long sessionId) {
        Session session = findSession(sessionId);
        return session != null && Objects.equals(session.getType(), SessionTypeEnum.STORE.getCode());
    }

    /**
     * 从数据库查询我的会话 更新缓存
     *
     * @param userId
     * @param sysId
     * @param sysId
     * @param size   未
     * @return
     */
    public List<Long> findDbUserSessionIdAndUpdateRedis(String userId, Integer sysId, Integer size) {
        RLock addUserSessionIdsZsetLock = sessionRedisService.getAddUserSessionIdsZsetLock(userId, sysId);
        try {
            addUserSessionIdsZsetLock.lock(1, TimeUnit.MINUTES);
            List<Long> userSessionIds = sessionRedisService.getUserSessionIds(userId, sysId);
            if (!userSessionIds.isEmpty()) {  // 双层校验防止并发情况下多次查询数据库并写缓存浪费性能
                return userSessionIds;
            }
            List<SessionUserBO> mySession = baseMapper.findMySession(userId, sysId, size);
            sessionRedisService.addUserSessionIds(userId, sysId, mySession);
            return mySession.stream().map(SessionUserBO::getSessionId).collect(Collectors.toList());
        } finally {
            addUserSessionIdsZsetLock.unlock();
        }
    }

    /**
     * 按更新时间倒序查询数据库用户的未删除的会话id
     *
     * @param userId
     * @param sysId
     * @return
     */
    public List<Long> findDbUserSessionId(String userId, Integer sysId, Integer size) {
        return baseMapper.findUserSessionId(userId, sysId, size);
    }

    /**
     * 查询数据库用户所有的会话id 包含删除的和未删除的
     *
     * @param userId
     * @param sysId
     * @return
     */
    public Set<Long> findDbUserAllSessionId(String userId, Integer sysId) {
        return baseMapper.findUserAllSessionId(userId, sysId);
    }

    /**
     * 从数据库查询我的会话id 更新到缓存 需要对比数据库数据是否落后当前数据
     *
     * @param userId
     * @param sysId
     * @param sysId
     * @param size   未
     * @return
     */
    public void loadDbUserSessionId2Redis(String userId, Integer sysId, Integer size, Long sessionId, Long updateTime) {
        RLock addUserSessionIdsZsetLock = sessionRedisService.getAddUserSessionIdsZsetLock(userId, sysId);
        try {
            addUserSessionIdsZsetLock.lock(1, TimeUnit.MINUTES);
            List<SessionUserBO> dbMySession = baseMapper.findMySession(userId, sysId, size);
            List<Long> dbMySessionIds = dbMySession.stream().map(SessionUserBO::getSessionId).collect(Collectors.toList());
            if (dbMySessionIds.isEmpty() || !dbMySessionIds.contains(sessionId)) {
                // 数据库数据落后当前数据 数据还在mq中
                SessionUserBO sessionUserBO = new SessionUserBO();
                sessionUserBO.setSessionId(sessionId);
                sessionUserBO.setUserId(userId);
                sessionUserBO.setUpdateTimeStamp(updateTime);
                dbMySession.add(sessionUserBO);
            }
            sessionRedisService.addUserSessionIds(userId, sysId, dbMySession);
        } finally {
            addUserSessionIdsZsetLock.unlock();
        }
    }

    /**
     * 从数据查询会话 更新缓存
     *
     * @param sessionId
     * @return
     */
    public Session findDbByIdAndUpdateRedis(Long sessionId) {
        Session session = getById(sessionId);
        if (session != null) {
            sessionRedisService.addSession(session);
        }
        return session;
    }

   /**
     * 从数据批量查询会话 更新缓存
     *
     * @param sessionIds
     * @return
     */
    public List<Session> findDbByIdsAndUpdateRedis(List<Long> sessionIds) {
        List<Session> sessions = listByIds(sessionIds);
        if (!sessions.isEmpty()) {
            for (Session session : sessions) {
                sessionRedisService.addSession(session);
            }
        }
        return sessions;
    }


    /**
     * 查询会话列表 优先查询缓存 其次查询数据库
     *
     * @param userId
     * @param sysId
     * @param size
     * @return
     */
    public List<Session> findMySession(String userId, Integer sysId, Integer size) {
        List<Long> sessionIds;
        if (size != null && size > USER_SESSION_CACHE_SIZE) {
            // 查库
            sessionIds = findDbUserSessionId(userId, sysId, size);
        } else {
            // 查缓存
            sessionIds = sessionRedisService.getUserSessionIds(userId, sysId);
            if (sessionIds.isEmpty()) {
                // 从数据库查询
                sessionIds = findDbUserSessionIdAndUpdateRedis(userId, sysId, size);
            }
        }
        return sessionRedisService.getSessions(sessionIds);
    }

    /**
     * 查询会话 优先缓存
     *
     * @param sessionId
     * @return
     */
    public Session findSession(Long sessionId) {
        Session session = sessionRedisService.getSession(sessionId);
        if (session == null) {
            // 从数据查询 更新缓存
            session = findDbByIdAndUpdateRedis(sessionId);
        }
        return session;
    }

    /**
     * 发送消息的场景下时 查询发送人和接收人的会话id
     *
     * @param fromId
     * @param toId
     * @param sysId
     * @return
     */
    public Long findSendMessageRelationSessionId(String fromId, String toId, Integer sysId) {
        List<Long> fromUserSessionIds = sessionRedisService.getUserSessionIds(fromId, sysId);
        List<Long> toUserSessionIds = sessionRedisService.getUserSessionIds(toId, sysId);
        fromUserSessionIds.retainAll(toUserSessionIds);
        if (fromUserSessionIds.isEmpty()) {
            return null;
        }
        return fromUserSessionIds.iterator().next();
    }

    /**
     * 检查会话是否属于该用户
     *
     * @param sessionId
     * @param userId
     * @param systemId
     * @return 会话是否属于该用户
     */
    public boolean sessionOfUser(Long sessionId, String userId, int systemId) {
        Set<String> sessionUserIds = sessionRedisService.getSessionUserIds(sessionId);
        if (sessionUserIds.isEmpty()) {
            sessionUserIds = sessionUserService.findDbSessionUserIdsAndUpdateRedis(sessionId);
            return sessionUserIds.contains(userId);
        } else {
            return sessionUserIds.contains(userId);
        }
    }

    /**
     * 创建客服会话
     *
     * @param userId
     * @param storeId
     * @param sysId
     * @param now
     * @param handshakeParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Session createStoreSession(String userId, String storeId, Integer sysId, Long now, HandshakeParam handshakeParam) {
        Session session = generateSession(userId, userId, sysId, now, SessionTypeEnum.STORE);
        save(session);
        StoreConfig storeConfig = storeConfigService.getById(Long.parseLong(storeId));
        sessionUserService.createStoreSessionUser(handshakeParam, storeConfig, session);
        return session;
    }

    @Transactional(rollbackFor = Exception.class)
    public Session createSingleSession(CreateSessionMessage createSessionMessage) {
        Session session = findDbSingleSession(createSessionMessage.getToId(), createSessionMessage.getFromId(), createSessionMessage.getSysId());
        if (session != null) {
            return session;
        }

        long now = System.currentTimeMillis();
        session = generateSession(createSessionMessage.getFromId(), createSessionMessage.getFromId(), createSessionMessage.getSysId(), now, SessionTypeEnum.SINGLE);
        save(session);
        List<SessionUser> sessionUsers = sessionUserService.createSingleSessionUser(session, createSessionMessage);
        addSession2Redis(session, sessionUsers);
        return session;
    }

    private Session findDbSingleSession(String toId, String fromId, Integer sysId) {
        return baseMapper.findSingleSession(toId, fromId, sysId);
    }

    public void addSession2Redis(Session session, List<SessionUser> sessionUsers) {
        for (SessionUser sessionUser : sessionUsers) {
            sessionRedisService.addSessionUser(sessionUser);
        }
        Set<String> sessionUserIds = sessionUsers.stream().map(SessionUser::getUserId).collect(Collectors.toSet());
        sessionRedisService.updateSessionUserIds(session.getId(), sessionUserIds);
        sessionRedisService.addSession(session);
        sessionRedisService.updateUserSessionIdSort(session.getId(), session.getUpdateTimeStamp(), sessionUserIds, session.getSysId());
    }

    public Session generateSession(String createUser, String updateUser, Integer sysId, Long now, SessionTypeEnum type) {
        Session session = new Session();
        session.setId(iIdService.generateSessionId());
        session.setSysId(sysId);
        session.setType(type.getCode());
        session.setIsDeleted(false);
        session.setUpdateTimeStamp(now);
        session.setCreateTimeStamp(now);
        session.setCreateUser(createUser);
        session.setUpdateUser(updateUser);
        return session;
    }

    /**
     * 更新会话是否已删除状态
     *
     * @param createUser
     * @param sessionId
     * @param isDelete
     */
    public boolean updateIsDelete(String createUser, Long sessionId, boolean isDelete) {
        lambdaUpdate()
                .eq(Session::getId, sessionId)
                .set(Session::getIsDeleted, isDelete)
                .update();
        return sessionUserService.updateIsDelete(createUser, sessionId, isDelete);
    }

    /**
     * 查询用户和商铺的客服会话
     * todo 查库需要优化
     *
     * @param userId
     * @param storeId
     * @param sysId
     * @return
     */
    public Long findDbUserStoreSessionId(String userId, String storeId, Integer sysId) {
        Set<Long> userStoreSessions = baseMapper.findUserAllStoreSessionId(userId, sysId);
        Set<Long> storeSessions = baseMapper.findUserAllStoreSessionId(storeId, sysId);
        userStoreSessions.retainAll(storeSessions);
        if (userStoreSessions.isEmpty()) {
            return null;
        }
        return userStoreSessions.iterator().next();
    }

    public Long findStoreUserSessionId(Integer sysId, String storeId, String userId) {
        return baseMapper.findStoreUserSessionId(sysId, storeId, userId);
    }


    public List<Session> findUnreadSession(Set<Long> sessionIds, String storeId) {
        if (sessionIds.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.findUnreadSession(sessionIds, storeId);
    }

    public List<Session> findWithinTimes(Set<Long> sessionIds, int withinTimeHours, int sessionWithinTimeSize, String storeId) {
        if (sessionIds.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.findWithinTimes(sessionIds, withinTimeHours, sessionWithinTimeSize, storeId);
    }

    /**
     * 查询指定人 指定会话id 指定会话个数 指定时间段内会话列表
     *
     * @param storeId
     * @param sessionIds
     * @return
     */
    public List<Session> findBySessionIdsAndUserId(String storeId, Set<Long> sessionIds) {
        if (sessionIds.isEmpty()) {
            return Collections.emptyList();
        }
        return baseMapper.findBySessionIdsAndUserId(storeId, sessionIds);
    }

    /**
     * 查询第三方系统发送消息时接收人和发送人是否有会话id交集
     *
     * @param sysId 系统
     * @param fromId 发送人
     * @param toId 接收人
     * @param sessionTypeEnum 会话类型
     * @param sessionPayload 元数据
     * @return 会话id
     */
    public Long findDbSessionId(Integer sysId, String fromId, String toId, SessionTypeEnum sessionTypeEnum, String sessionPayload) {
        return baseMapper.findDbSessionId(sysId, fromId, toId, sessionTypeEnum.getCode(), sessionPayload);
    }
}

