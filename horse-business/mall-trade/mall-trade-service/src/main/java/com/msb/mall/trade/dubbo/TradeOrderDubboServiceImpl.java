package com.msb.mall.trade.dubbo;

import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.api.dubbo.TradeOrderDubboService;
import com.msb.mall.trade.api.model.*;
import com.msb.mall.trade.enums.OrderStatusEnum;
import com.msb.mall.trade.enums.RefundStatusEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.service.OrderStatisticsService;
import com.msb.mall.trade.service.RefundOrderService;
import com.msb.mall.trade.service.SalesStatisticsService;
import com.msb.mall.trade.service.TradeOrderService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易订单DubboService
 *
 * @author shumengjiao
 */
@DubboService
public class TradeOrderDubboServiceImpl implements TradeOrderDubboService {

    @Resource
    private TradeOrderService tradeOrderService;

    @Resource
    private SalesStatisticsService salesStatisticsService;

    @Resource
    private OrderStatisticsService orderStatisticsService;

    @Resource
    private RefundOrderService refundOrderService;

    /**
     * 查询订单待处理事务数据
     * @return 待处理事务数据
     */
    @Override
    public TradeOrderWaitHandleDO getWaitHandleOrderCount() {
        int waitPayCount = tradeOrderService.countOfStatistics(OrderStatusEnum.UNPAID);
        int waitShipCount = tradeOrderService.countOfStatistics(OrderStatusEnum.PAID);
        int shippedCount = tradeOrderService.countOfStatistics(OrderStatusEnum.DELIVERED);
        int waitRefundCount = refundOrderService.countOfStatistics(RefundStatusEnum.APPLY);
        int waitReturnGoodsCount = refundOrderService.countOfStatistics(RefundStatusEnum.IN_RETURN);
        return new TradeOrderWaitHandleDO()
                .setWaitPayCount(waitPayCount)
                .setWaitShipCount(waitShipCount)
                .setShippedCount(shippedCount)
                .setWaitRefundCount(waitRefundCount)
                .setWaitReturnGoodsCount(waitReturnGoodsCount);
    }

    /**
     * 根据时间区间从交易订单中获取销售总额
     * @param beginDateTime 开始时间
     * @param endDateTime 结束时间
     * @return 销售总额
     */
    @Override
    public BigDecimal getSalesByTradeOrder(LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        return tradeOrderService.getSales(beginDateTime, endDateTime);
    }

    /**
     * 根据时间区间从订单统计表中获取销售总额
     * @param beginDate 时间区间-开始
     * @param endDate 时间区间-结束
     * @return 销售总额
     */
    @Override
    public BigDecimal getSalesByOrderStatistics(LocalDate beginDate, LocalDate endDate) {
        return salesStatisticsService.getSales(beginDate, endDate);
    }

    /**
     * 从交易订单表统计订单数量
     * @param beginDateTime 开始时间
     * @param endDateTime 结束时间
     * @return 订单数量
     */
    @Override
    public Integer getOrderCount(LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        return tradeOrderService.getOrderCount(beginDateTime, endDateTime);
    }

    /**
     * 从统计表查询订单数量
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 订单数量
     */
    @Override
    public Integer getOrderCountByStatistics(LocalDate beginDate, LocalDate endDate) {
        return orderStatisticsService.getOrderCount(beginDate, endDate);
    }

    /**
     * 按小时统计销售额
     * @param dayOfStatics 统计的日期
     * @return 销售额
     */
    @Override
    public List<SalesStatisticsByHourDO> listSalesStatisticsByHour(LocalDate dayOfStatics) {
        return tradeOrderService.listSalesStatisticsByHour(dayOfStatics);
    }

    /**
     * 按天统计销售额
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 销售额
     */
    @Override
    public List<SalesStatisticsByDayDO> listSalesStatisticsByDay(LocalDate beginDate, LocalDate endDate) {
        return salesStatisticsService.listSalesStatisticsByDay(beginDate, endDate);
    }

    /**
     * 按小时统计订单数量
     * @param dayOfStatics 统计的日期
     * @return 订单数量
     */
    @Override
    public List<OrderStatisticsByHourDO> listOrderStatisticsByHour(LocalDate dayOfStatics) {
        return tradeOrderService.listOrderCountStatisticsByHour(dayOfStatics);
    }

    /**
     * 按天获取订单数量
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 订单数量
     */
    @Override
    public List<OrderStatisticsByDayDO> listOrderStatisticsByDay(LocalDate beginDate, LocalDate endDate) {
        return orderStatisticsService.listOrderStatisticsByDay(beginDate, endDate);
    }

    /**
     * 修改订单状态
     * @param orderId 订单id
     * @param targetStatus 目标状态
     * @param originStatus 原来的状态
     */
    @Override
    public void updateOrderStatus(Long orderId, Integer targetStatus, Integer originStatus) {
        // 将状态转换成枚举
        OrderStatusEnum[] values = OrderStatusEnum.values();
        OrderStatusEnum targetEnum = null;
        OrderStatusEnum originEnum = null;
        for (OrderStatusEnum value : values) {
            Integer code = value.getCode();
            if (code.equals(targetStatus)) {
                targetEnum = value;
            }
            if (code.equals(originStatus)) {
                originEnum = value;
            }
        }
        // 更新订单状态
        boolean update = tradeOrderService.compareAndUpdateOrderStatus(orderId, targetEnum, originEnum);
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_STATUS_EXCEPTION);
    }


}
