package com.msb.im.module.waiter.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.im.module.waiter.mapper.UserWaiterHistoryLogMapper;
import com.msb.im.module.waiter.model.bo.UserWaiterBo;
import com.msb.im.module.waiter.model.entity.UserWaiterHistoryLog;
import com.msb.im.module.waiter.model.vo.WaiterStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户和客服的分配历史记录
 *
 * @author zhou miao
 * @date 2022/05/11
 */
@Service
@Slf4j
public class UserWaiterHistoryLogService extends ServiceImpl<UserWaiterHistoryLogMapper, UserWaiterHistoryLog> {

    public void add(Long storeId, String userId, String allocateWaiterId, String createUser, String updateUser) {
        LocalDateTime now = LocalDateTime.now();
        UserWaiterHistoryLog userWaiterHistoryLog = new UserWaiterHistoryLog();
        userWaiterHistoryLog.setUserId(userId);
        userWaiterHistoryLog.setStoreId(storeId);
        userWaiterHistoryLog.setWaiterId(allocateWaiterId);
        userWaiterHistoryLog.setCreateUser(createUser);
        userWaiterHistoryLog.setUpdateUser(updateUser);
        userWaiterHistoryLog.setCreateTime(now);
        userWaiterHistoryLog.setUpdateTime(now);
        save(userWaiterHistoryLog);
    }


    /**
     * 接待统计
     *
     * @return
     */
    public WaiterStatisticsVO waiterStatistics(Long storeId, Long waiterId) {
        Long historyNum = baseMapper.historyNum(storeId, waiterId);
        Long historyPeopleNum = baseMapper.historyPeopleNum(storeId, waiterId);
        Long todayNum = baseMapper.todayNum(storeId, waiterId);
        Long todayPeopleNum = baseMapper.todayPeopleNum(storeId, waiterId);
        return WaiterStatisticsVO.builder().historyNum(historyNum).historyPeopleNum(historyPeopleNum).todayNum(todayNum).todayPeopleNum(todayPeopleNum).build();
    }

    /**
     * 查询客服历史接待会话
     *
     * @param storeId 商铺id
     * @param waiterId
     * @param start
     * @param end
     * @return
     */
    public Set<UserWaiterBo> findWaiterHistorySession(Long storeId, String waiterId, LocalDateTime start, LocalDateTime end) {
        return baseMapper.findWaiterHistorySession(storeId, waiterId, start, end);
    }
}
