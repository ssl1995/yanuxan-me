package com.msb.mall.trade.api.dubbo;

import com.msb.mall.trade.api.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易订单dubbo服务
 * @author shumengjiao
 */
public interface TradeOrderDubboService {

    /**
     * 查询订单待处理事务数据
     * @return TradeOrderWaitHandleDO
     */
    TradeOrderWaitHandleDO getWaitHandleOrderCount();

    /**
     * 根据时间区间从交易订单中获取销售总额
     * @param beginDateTime 开始时间
     * @param endDateTime 结束时间
     * @return 销售总额
     */
    BigDecimal getSalesByTradeOrder(LocalDateTime beginDateTime, LocalDateTime endDateTime);

    /**
     * 根据时间区间从销售额统计表中获取销售总额
     * @param beginDate 时间区间-开始
     * @param endDate 时间区间-结束
     * @return
     */
    BigDecimal getSalesByOrderStatistics(LocalDate beginDate, LocalDate endDate);

    /**
     * 根据时间区间从查询订单总数量
     * @param beginDateTime
     * @param endDateTime
     * @return
     */
    Integer getOrderCount(LocalDateTime beginDateTime, LocalDateTime endDateTime);

    /**
     * 根据时间区间从订单统计表中获取订单总数量
     * @param beginDate
     * @param endDate
     * @return 订单总数量
     */
    Integer getOrderCountByStatistics(LocalDate beginDate, LocalDate endDate);

    /**
     * 按小时获取销售额
     * @param dayOfStatics 统计的日期
     * @return 销售额
     */
    List<SalesStatisticsByHourDO> listSalesStatisticsByHour(LocalDate dayOfStatics);

    /**
     * 按天获取销售额
     * @param beginDate
     * @param endDate
     * @return
     */
    List<SalesStatisticsByDayDO> listSalesStatisticsByDay(LocalDate beginDate, LocalDate endDate);

    /**
     * 按照小时获取订单数量
     * @param dayOfStatics 统计的日期
     * @return
     */
    List<OrderStatisticsByHourDO> listOrderStatisticsByHour(LocalDate dayOfStatics);

    /**
     * 按天获取订单数量
     * @param beginDate
     * @param endDate
     * @return
     */
    List<OrderStatisticsByDayDO> listOrderStatisticsByDay(LocalDate beginDate, LocalDate endDate);

    /**
     * 修改订单状态
     * @param orderId 订单id
     * @param targetStatus 目标状态
     * @param originStatus 原来的状态
     */
    void updateOrderStatus(Long orderId, Integer targetStatus, Integer originStatus);
}
