package com.msb.im.module.waiter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.module.waiter.model.bo.UserWaiterBo;
import com.msb.im.module.waiter.model.bo.WaiterBO;
import com.msb.im.module.waiter.model.entity.UserWaiterHistoryLog;
import com.msb.im.module.waiter.model.vo.WaiterStatisticsVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 商铺和客服的关联历史
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
public interface UserWaiterHistoryLogMapper extends BaseMapper<UserWaiterHistoryLog> {

    /**
     * 历史接待次数
     *
     * @param storeId
     * @param waiterId
     * @return
     */
    Long historyNum(@Param("storeId") Long storeId, @Param("waiterId") Long waiterId);

    /**
     * 历史接待人数
     *
     * @param storeId
     * @param waiterId
     * @return
     */
    Long historyPeopleNum(@Param("storeId") Long storeId, @Param("waiterId") Long waiterId);

    /**
     * 今日接待次数
     *
     * @param storeId
     * @param waiterId
     * @return
     */
    Long todayNum(@Param("storeId") Long storeId, @Param("waiterId") Long waiterId);

    /**
     * 今日接待人数
     *
     * @param storeId
     * @param waiterId
     * @return
     */
    Long todayPeopleNum(@Param("storeId") Long storeId, @Param("waiterId") Long waiterId);

    /**
     * 查询客服历史接待会话id
     *
     * @param storeId
     * @param waiterId
     * @param start
     * @param end
     * @return
     */
    Set<UserWaiterBo> findWaiterHistorySession(@Param("storeId") Long storeId, @Param("waiterId") String waiterId, @Param("startTime") LocalDateTime start, @Param("endTime") LocalDateTime end);

}
