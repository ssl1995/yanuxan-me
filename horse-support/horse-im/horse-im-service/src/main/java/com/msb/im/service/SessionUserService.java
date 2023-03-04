package com.msb.im.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.im.api.dto.UpdateSessionUserDTO;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.context.ApiContext;
import com.msb.im.convert.SessionUserConvert;
import com.msb.im.mapper.SessionUserMapper;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.module.chat.model.CreateSessionMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.mq.model.ImMqMessage;
import com.msb.im.mq.producer.ImMqProducerService;
import com.msb.im.mq.service.UpdateUnReadImMqMessageHandleService;
import com.msb.im.netty.model.HandshakeParam;
import com.msb.im.redis.SessionRedisService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (HorseImSessionUser)表服务实现类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:34
 */
@Service("sessionUserService")
@Slf4j
public class SessionUserService extends ServiceImpl<SessionUserMapper, SessionUser> {

    @Resource
    private SessionRedisService sessionRedisService;
    @Resource
    private ImMqProducerService imMqProducerService;
    @Resource
    private IIdService iIdService;
    @Resource
    private UpdateUnReadImMqMessageHandleService updateUnReadHandleService;
    @Resource
    private SessionUserConvert sessionUserConvert;

    public SessionUser generateFromMqSessionUser(SendMessageBO sendMessageBO, Long sessionId, Long nowTimestamp) {
        Long fromSessionUserId = iIdService.generateSessionUserId();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setId(fromSessionUserId);
        sessionUser.setSessionId(sessionId);
        sessionUser.setUserId(sendMessageBO.getFromId());
        sessionUser.setCreateTimeStamp(nowTimestamp);
        sessionUser.setUpdateTimeStamp(nowTimestamp);
        sessionUser.setCreateUser(sendMessageBO.getFromId());
        sessionUser.setUpdateUser(sendMessageBO.getFromId());
        sessionUser.setUnReadCount(0L);
        sessionUser.setUserAvatar(sendMessageBO.getFromAvatar());
        sessionUser.setUserNickname(sendMessageBO.getFromNickname());
        sessionUser.setUnReadCount(0L);
        sessionUser.setRelationUserId(sendMessageBO.getToId());
        sessionUser.setIsDeleted(false);
        return sessionUser;
    }

    public SessionUser generateToMqSessionUser(SendMessageBO sendMessageBO, Long sessionId, Long nowTimestamp) {
        Long fromSessionUserId = iIdService.generateSessionUserId();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setId(fromSessionUserId);
        sessionUser.setSessionId(sessionId);
        sessionUser.setUserId(sendMessageBO.getToId());
        sessionUser.setCreateTimeStamp(nowTimestamp);
        sessionUser.setUpdateTimeStamp(nowTimestamp);
        sessionUser.setCreateUser(sendMessageBO.getFromId());
        sessionUser.setUpdateUser(sendMessageBO.getFromId());
        sessionUser.setUnReadCount(1L);
        sessionUser.setUserAvatar(sendMessageBO.getToAvatar());
        sessionUser.setUserNickname(sendMessageBO.getToNickname());
        sessionUser.setRelationUserId(sendMessageBO.getFromId());
        sessionUser.setIsDeleted(false);
        return sessionUser;
    }

    /**
     * 从数据库查询未读数 更新缓存
     *
     * @param userId
     * @param sessionId
     * @return
     */
    public Long findDbUnreadCountAndUpdateRedis(String userId, Long sessionId) {
        RLock addUserSessionUnreadLock = sessionRedisService.getAddUserSessionUnreadLock(userId, sessionId);
        try {
            addUserSessionUnreadLock.lock(1, TimeUnit.MINUTES);
            if (sessionRedisService.existSessionUserUnreadCount(userId, sessionId)) { // 防止缓存失效 多次查询数据库多次更新缓存
                return sessionRedisService.getSessionUserUnreadCount(userId, sessionId);
            }
            SessionUser byUserIdAndSessionId = findDbByUserIdAndSessionId(userId, sessionId);
            long unReadCount;
            if (byUserIdAndSessionId == null) {
                unReadCount = 0;
            } else {
                unReadCount = byUserIdAndSessionId.getUnReadCount();
            }
            // 更新缓存
            sessionRedisService.updateSessionUserUnreadCount(userId, sessionId, unReadCount);
            return unReadCount;
        } finally {
            addUserSessionUnreadLock.unlock();
        }

    }

    /**
     * 从数据库查询未读数 和数据库未读数的更新时间进行对比是否需要更新最新缓存
     *
     * @param userId
     * @param sessionId
     * @param now
     * @param increment
     * @return
     */
    public void loadDbUserSessionUnread2Redis(String userId, Long sessionId, Long now, Integer increment) {
        RLock addUserSessionUnreadLock = sessionRedisService.getAddUserSessionUnreadLock(userId, sessionId);
        try {
            addUserSessionUnreadLock.lock(1, TimeUnit.MINUTES);
            SessionUser dbSessionUser = findDbByUserIdAndSessionId(userId, sessionId);
            long dbUnReadCount = 0;
            if (dbSessionUser != null) {
                dbUnReadCount = dbSessionUser.getUnReadCount();
            }

            if (dbSessionUser == null || dbSessionUser.getUpdateTimeStamp() < now) {
                // 数据库版本落后 数据还在mq中 更新缓存时需要考虑
                dbUnReadCount += increment;
            }
            sessionRedisService.updateSessionUserUnreadCount(userId, sessionId, dbUnReadCount);
        } finally {
            addUserSessionUnreadLock.unlock();
        }

    }

    public SessionUser findDbByUserIdAndSessionId(String userId, Long sessionId) {
        LambdaQueryWrapper<SessionUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SessionUser::getSessionId, sessionId);
        queryWrapper.eq(SessionUser::getUserId, userId);
        return getOne(queryWrapper);
    }

    /**
     * 通过会话id和用户id查询会话用户关联 优先查询缓存
     *
     * @param sessionId 会话id
     * @param userId    用户id
     * @return 会话用户关联
     */
    public SessionUser findBySessionIdAndUserId(Long sessionId, String userId) {
        SessionUser sessionUser = sessionRedisService.getSessionUser(sessionId, userId);
        if (sessionUser != null) {
            return sessionUser;
        }
        return findDbBySessionAndUserAndUpdateRedis(sessionId, userId);
    }

    /**
     * 从数据库查询会话用户关联 然后更新缓存
     *
     * @param sessionId 会话id
     * @param userId 用户id
     * @return 会话用户关联
     */
    public SessionUser findDbBySessionAndUserAndUpdateRedis(Long sessionId, String userId) {
        SessionUser dbSessionUser = findDbByUserIdAndSessionId(userId, sessionId);
        sessionRedisService.addSessionUser(dbSessionUser);
        return dbSessionUser;
    }

    /**
     * 查询当前最大id 即数据库记录数
     *
     * @return
     */
    public Long findDbMaxId() {
        return baseMapper.findMaxId();
    }

    /**
     * 从数据库查询会话的用户id 加载到缓存
     *
     * @param sessionId
     * @return
     */
    public Set<String> findDbSessionUserIdsAndUpdateRedis(Long sessionId) {
        RLock addSessionUsersSetLock = sessionRedisService.getAddSessionUsersSetLock(sessionId);
        try {
            addSessionUsersSetLock.lock(1, TimeUnit.MINUTES);
            // 防止其他查询已经加载了缓存，提高性能
            Set<String> sessionUserIds = sessionRedisService.getSessionUserIds(sessionId);
            if (!sessionUserIds.isEmpty()) {
                return sessionUserIds;
            }
            Set<String> dbSessionUserIds = baseMapper.findUserIdBySessionId(sessionId);
            if (dbSessionUserIds.isEmpty()) {
                log.warn("会话用户不存在 {} {}", sessionId, dbSessionUserIds);
                return dbSessionUserIds;
            }
            sessionRedisService.updateSessionUserIds(sessionId, dbSessionUserIds);
            return dbSessionUserIds;
        } finally {
            addSessionUsersSetLock.unlock();
        }
    }

    /**
     * 从数据库加载会话的用户id 同时会
     *
     * @param sessionId
     * @param addUserIds
     * @return
     */
    public void loadDbSessionUserIds2Redis(Long sessionId, Set<String> addUserIds) {
        RLock addSessionUsersSetLock = sessionRedisService.getAddSessionUsersSetLock(sessionId);
        try {
            addSessionUsersSetLock.lock(1, TimeUnit.MINUTES);
            Set<String> dbSessionUserIds = baseMapper.findUserIdBySessionId(sessionId);
            if (dbSessionUserIds.isEmpty() || !dbSessionUserIds.containsAll(addUserIds)) {
                // 数据库数据落后 数据还在mq中
                dbSessionUserIds.addAll(addUserIds);
            }
            if (dbSessionUserIds.isEmpty()) {
                log.warn("会话用户为空 {} {}", sessionId, dbSessionUserIds);
                return;
            }
            sessionRedisService.updateSessionUserIds(sessionId, dbSessionUserIds);
        } finally {
            addSessionUsersSetLock.unlock();
        }
    }

    /**
     * 查询会话未读数 优先缓存
     *
     * @param userId
     * @param sessionId
     * @return
     */
    public Long findUnreadCount(String userId, Long sessionId) {
        Long sessionUserUnreadCount = sessionRedisService.getSessionUserUnreadCount(userId, sessionId);
        if (sessionUserUnreadCount == null) {
            sessionUserUnreadCount = findDbUnreadCountAndUpdateRedis(userId, sessionId);
        }
        return sessionUserUnreadCount;
    }


    /**
     * 查询会话中的用户id 优先缓存
     *
     * @param sessionId
     * @return
     */
    public Set<String> findSessionUserIds(Long sessionId) {
        Set<String> sessionUserIds = sessionRedisService.getSessionUserIds(sessionId);
        if (sessionUserIds.isEmpty()) {
            return findDbSessionUserIdsAndUpdateRedis(sessionId);
        }
        return sessionUserIds;
    }

    /**
     * 更新消息已读
     *
     * @param sessionId
     * @param userId
     * @return
     */
    public boolean readMessage(Long sessionId, String userId) {
        // 组装mq消息
        ImMqMessage imMqMessage = updateUnReadHandleService.createUpdateUnReadImMqMessage(sessionId, userId);

        // 发送mq
        imMqProducerService.sendMq(imMqMessage, () -> updateUnReadHandleService.handle2Db(imMqMessage));

        // 存入缓存
        updateUnReadHandleService.handle2Cache(imMqMessage);
        return true;
    }

    /**
     * 更新数据库会话消息的未读数量
     *
     * @param sessionId
     * @param userId
     * @param unreadCount
     * @param now
     * @param updateUser
     */
    public void updateDbUnreadCount(Long sessionId, String userId, Long unreadCount, Long now, String updateUser) {
        baseMapper.updateUnreadCount(sessionId, userId, unreadCount, now, updateUser);
    }

    /**
     * @param sessionUsers
     */
    public void addSessionUsers(List<SessionUser> sessionUsers) {
        if (sessionUsers.isEmpty()) {
            return;
        }
        saveBatch(sessionUsers);
    }

    /**
     * 根据条件更新用户会话关联数据
     *
     * @param sessionUser
     * @param condition
     * @param isAddUnread 是否需要未读数加1
     */
    public void updateByCondition(SessionUser sessionUser, SessionUser condition, boolean isAddUnread) {
        baseMapper.updateByCondition(sessionUser, condition, isAddUnread);
    }

    /**
     * 用户删除会话
     *
     * @param userId
     * @param sessionId
     * @param systemId
     */
    public void deleteSession(String userId, Long sessionId, Integer systemId) {
        deleteDbSession(userId, sessionId);
        sessionRedisService.removeUserSessionId(userId, sessionId, systemId);
    }


    /**
     * 逻辑删除用户的session
     *
     * @param userId
     * @param sessionId
     */
    public void deleteDbSession(String userId, Long sessionId) {
        lambdaUpdate()
                .eq(SessionUser::getUserId, userId)
                .eq(SessionUser::getSessionId, sessionId)
                .set(SessionUser::getIsDeleted, true)
                .update();
    }

    /**
     * 更新会话是否已删除状态
     *
     * @param createUser
     * @param sessionId
     * @param isDelete
     */
    public boolean updateIsDelete(String createUser, Long sessionId, boolean isDelete) {
        return lambdaUpdate()
                .eq(SessionUser::getUserId, createUser)
                .eq(SessionUser::getSessionId, sessionId)
                .set(SessionUser::getIsDeleted, isDelete)
                .update();
    }

    /**
     * 创建会话单人会话用户关联
     *
     * @param handshakeParam
     * @param storeConfig
     * @param session
     */
    public boolean createStoreSessionUser(HandshakeParam handshakeParam, StoreConfig storeConfig, Session session) {
        ArrayList<SessionUser> sessionUsers = new ArrayList<>();
        SessionUser from = generateSessionUser(session, handshakeParam.getUser(), handshakeParam.getAvatar(), handshakeParam.getNickname(), storeConfig.getId().toString());
        SessionUser to = generateSessionUser(session, storeConfig.getId().toString(), storeConfig.getAvatar(), storeConfig.getName(), handshakeParam.getUser());
        sessionUsers.add(from);
        sessionUsers.add(to);
        return saveBatch(sessionUsers);
    }

    public boolean sessionExistByFromIdAndToId(Long fromId, Long toId) {
        List<SessionUser> sessionUsers = lambdaQuery().eq(SessionUser::getUserId, fromId).list();
        Set<Long> toSessionIds = lambdaQuery().eq(SessionUser::getUserId, toId).list().stream().map(SessionUser::getSessionId).collect(Collectors.toSet());
        for (SessionUser sessionUser : sessionUsers) {
            if (toSessionIds.contains(sessionUser.getSessionId())) {
                log.info("用户的会话存在 {} {}", fromId, toId);
                return true;
            }
        }
        log.info("用户的会话不存在 {} {}", fromId, toId);
        return false;
    }

    public int updateUserData(UpdateSessionUserDTO updateSessionUserDTO) {
        List<SessionUser> sessionUsers = baseMapper.findBySysIdAndUserId(updateSessionUserDTO.getSystemId(), updateSessionUserDTO.getUserId(), Lists.newArrayList(SessionTypeEnum.CUSTOM.getCode(), SessionTypeEnum.SINGLE.getCode(), SessionTypeEnum.GROUP.getCode()));
        SessionUser update = sessionUserConvert.toDo(updateSessionUserDTO);
        update.setUpdateUser(updateSessionUserDTO.getUserId());
        int count = 0;
        for (SessionUser sessionUser : sessionUsers) {
            LambdaUpdateWrapper<SessionUser> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(SessionUser::getId, sessionUser.getId());
            if (update(update, updateWrapper)) {
                sessionRedisService.removeSessionUser(sessionUser.getUserId(), sessionUser.getSessionId());
                count++;
            }
        }
        log.info("修改用户昵称 {} {}", updateSessionUserDTO, count);
        return count;
    }

    /**
     * 通过用户id和会话id 查询会话用户 优先查询缓存
     *
     * @param sessionIdAndUserId 用户id和会话id
     * @return 会话用户
     */
    public List<SessionUser> findSessionUser(Map<Long, String> sessionIdAndUserId) {
        List<SessionUser> sessionUsers = new ArrayList<>(sessionIdAndUserId.size());
        sessionIdAndUserId.forEach((sessionId, userId) -> {
            sessionUsers.add(findBySessionIdAndUserId(sessionId, userId));
        });
        return sessionUsers;
    }

    public List<SessionUser> createSingleSessionUser(Session session, CreateSessionMessage createSessionMessage) {
        SessionUser from = generateSessionUser(session, createSessionMessage.getFromId(), createSessionMessage.getFromAvatar(), createSessionMessage.getFromNickname(), createSessionMessage.getToId());
        SessionUser to = generateSessionUser(session, createSessionMessage.getToId(), createSessionMessage.getToAvatar(), createSessionMessage.getToNickname(), createSessionMessage.getFromId());
        ArrayList<SessionUser> sessionUsers = Lists.newArrayList(from, to);
        saveBatch(sessionUsers);
        return sessionUsers;
    }

    private SessionUser generateSessionUser(Session session, String fromId, String fromAvatar, String fromNickname, String toId) {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setId(iIdService.generateSessionUserId());
        sessionUser.setSessionId(session.getId());
        sessionUser.setUserId(fromId);
        sessionUser.setUserAvatar(fromAvatar);
        sessionUser.setUserNickname(fromNickname);
        sessionUser.setRelationUserId(toId);
        sessionUser.setUnReadCount(0L);
        sessionUser.setCreateTimeStamp(session.getUpdateTimeStamp());
        sessionUser.setUpdateTimeStamp(session.getUpdateTimeStamp());
        sessionUser.setIsDeleted(false);
        sessionUser.setCreateUser(fromId);
        sessionUser.setUpdateUser(fromId);
        return sessionUser;
    }


}

