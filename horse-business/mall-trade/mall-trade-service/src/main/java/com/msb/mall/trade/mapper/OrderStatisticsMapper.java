package com.msb.mall.trade.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.trade.model.entity.OrderStatistics;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

/**
 * 订单统计表(OrderStatistics)表数据库访问层
 *
 * @author shumengjiao
 * @since 2022-05-30 20:17:31
 */
public interface OrderStatisticsMapper extends BaseMapper<OrderStatistics> {

    /**
     * 查询订单总数
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 订单总数
     */
    @Select("SELECT SUM(count) FROM order_statistics WHERE record_date BETWEEN #{beginDate} AND #{endDate}")
    Integer getOrderCount(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

    /**
     * 根据每天查询订单数量，如果当天没有也返回0
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<OrderStatistics> listOrderStatisticsByDay(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);
}

