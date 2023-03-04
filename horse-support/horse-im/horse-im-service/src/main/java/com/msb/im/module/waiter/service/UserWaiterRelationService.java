package com.msb.im.module.waiter.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.im.module.waiter.model.bo.UserWaiterBo;
import com.msb.im.module.waiter.model.bo.WaiterBO;
import com.msb.im.module.waiter.model.entity.UserWaiterRelation;
import org.springframework.stereotype.Service;
import com.msb.im.module.waiter.mapper.UserWaiterRelationMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 用户和商铺客服绑定的服务类
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Service
public class UserWaiterRelationService extends ServiceImpl<UserWaiterRelationMapper, UserWaiterRelation> {

    public UserWaiterRelation findByUserAndStore(String userId, Long storeId) {
        LambdaQueryWrapper<UserWaiterRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWaiterRelation::getUserId, userId);
        queryWrapper.eq(UserWaiterRelation::getStoreId, storeId);
        return getOne(queryWrapper);
    }

    public List<UserWaiterRelation> findByWaiterAndStore(String waiterId, Long storeId) {
        LambdaQueryWrapper<UserWaiterRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWaiterRelation::getWaiterId, waiterId);
        queryWrapper.eq(UserWaiterRelation::getStoreId, storeId);
        return list(queryWrapper);
    }

    /**
     * 新增或者更新用户关联的客服
     *
     * @param waiterId
     * @param storeId
     * @param userId
     * @param sessionId
     */
    public void addOrUpdate(String waiterId, Long storeId, String userId, Long sessionId) {
        UserWaiterRelation userWaiterBo = findByUserAndStore(userId, storeId);
        if (userWaiterBo == null) {
            UserWaiterRelation add = createUserWaiter(waiterId, storeId, userId, sessionId);
            save(add);
        } else {
            updateByUserAndStore(waiterId, storeId, userId);
        }
    }

    public void updateByUserAndStore(String waiterId, Long storeId, String userId) {
        lambdaUpdate()
                .eq(UserWaiterRelation::getUserId, userId)
                .eq(UserWaiterRelation::getStoreId, storeId)
                .set(UserWaiterRelation::getWaiterId, waiterId)
                .set(UserWaiterRelation::getUpdateTime, LocalDateTime.now())
                .update();
    }

    private UserWaiterRelation createUserWaiter(String waiterId, Long storeId, String userId, Long sessionId) {
        LocalDateTime now = LocalDateTime.now();
        UserWaiterRelation userWaiterRelation = new UserWaiterRelation();
        userWaiterRelation.setWaiterId(waiterId);
        userWaiterRelation.setStoreId(storeId);
        userWaiterRelation.setUserId(userId);
        userWaiterRelation.setSessionId(sessionId);
        userWaiterRelation.setCreateUser(userId);
        userWaiterRelation.setUpdateUser(userId);
        userWaiterRelation.setCreateTime(now);
        userWaiterRelation.setUpdateTime(now);
        return userWaiterRelation;
    }

    /**
     * 通过会话id查询
     *
     * @param sessionIds
     * @return
     */
    public List<UserWaiterRelation> findBySessionIds(List<Long> sessionIds) {
        if (sessionIds.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery()
                .in(UserWaiterRelation::getSessionId, sessionIds)
                .list();
    }

    public void updateWaiterId(Long storeId, String userId, String toWaiterId, String updateUser, LocalDateTime updateTime) {
        lambdaUpdate()
                .eq(UserWaiterRelation::getStoreId, storeId)
                .eq(UserWaiterRelation::getUserId, userId)
                .set(UserWaiterRelation::getWaiterId, toWaiterId)
                .set(UserWaiterRelation::getUpdateUser, updateUser)
                .set(UserWaiterRelation::getUpdateTime, updateTime)
                .update();
    }

    public Long findSessionId(Long storeId, String userId) {
        return baseMapper.findSessionId(storeId, userId);
    }

    public UserWaiterRelation findBySessionId(Long sessionId) {
        LambdaQueryWrapper<UserWaiterRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWaiterRelation::getSessionId, sessionId);
        return getOne(queryWrapper);
    }
}
