package com.msb.mall.trade.job;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.trade.enums.OrderStatusEnum;
import com.msb.mall.trade.model.entity.TradeOrder;
import com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO;
import com.msb.mall.trade.service.TradeOrderLogisticsService;
import com.msb.mall.trade.service.TradeOrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易订单定时任务
 */
@Slf4j
@Component
public class TradeOrderJob {

    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private TradeOrderLogisticsService tradeOrderLogisticsService;

    /**
     * 订单超时自动关闭任务
     *
     * @author peng.xy
     * @date 2022/4/7
     */
    @XxlJob("orderPayExpireTask")
    public void orderPayExpireTask() {
        List<TradeOrder> list = tradeOrderService.lambdaQuery()
                .select(TradeOrder::getId)
                .eq(TradeOrder::getOrderStatus, OrderStatusEnum.UNPAID.getCode())
                .le(TradeOrder::getExpireTime, LocalDateTime.now())
                .last("limit 1000")
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(tradeOrder -> {
            Long orderId = tradeOrder.getId();
            try {
                tradeOrderService.expirePay(orderId);
                log.info("订单超时自动关闭成功，订单ID：{}", orderId);
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("订单超时自动关闭失败，订单ID：{}，错误信息：{}", orderId, e.getMessage());
            }
        });
    }

    /**
     * 订单收货到期，自动收货任务
     *
     * @return void
     * @author peng.xy
     * @date 2022/4/7
     */
    @XxlJob("orderReceiveExpireTask")
    public void orderReceiveExpireTask() {
        List<TradeOrder> list = tradeOrderService.lambdaQuery()
                .select(TradeOrder::getId)
                .eq(TradeOrder::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())
                .le(TradeOrder::getAutoReceiveTime, LocalDateTime.now())
                .last("limit 1000")
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(tradeOrder -> {
            Long orderId = tradeOrder.getId();
            try {
                tradeOrderService.expireReceive(orderId);
                log.info("订单到期自动收货成功，订单ID：{}", orderId);
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("订单到期自动收货失败，订单ID：{}，错误信息：{}", orderId, e.getMessage());
            }
        });
    }

    /**
     * 定时更新订单物流任务
     *
     * @author peng.xy
     * @date 2022/4/7
     */
    @XxlJob("orderRefreshLogisticsTask")
    public void orderRefreshLogisticsTask() {
        this.orderRefreshLogistics(1, 500);
    }

    private void orderRefreshLogistics(int pageIndex, int size) {
        Page<TradeOrder> page = tradeOrderService.lambdaQuery()
                .select(TradeOrder::getId)
                .eq(TradeOrder::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())
                .page(new Page<>(pageIndex, size));
        List<TradeOrder> list = page.getRecords();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(tradeOrder -> {
            Long orderId = tradeOrder.getId();
            try {
                LogisticsExpressQueryVO expressQueryVO = tradeOrderLogisticsService.refreshLogistics(orderId);
                log.info("定时更新订单物流，订单ID：{}，更新结果：{}", orderId, expressQueryVO.getIsSuccess());
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("定时更新订单物流失败，订单ID：{}，错误信息：{}", orderId, e.getMessage());
            }
        });
        if (page.hasNext()) {
            this.orderRefreshLogistics(++pageIndex, size);
        }
    }

    /**
     * 订单已收货到期，自动五星好评
     * @author shumengjiao
     * @date 2022/6/17
     */
    @XxlJob("orderReceivedExpireTask")
    public void orderReceivedExpireTask() {
        List<TradeOrder> list = tradeOrderService.lambdaQuery()
                .select(TradeOrder::getId)
                .eq(TradeOrder::getOrderStatus, OrderStatusEnum.RECEIVING.getCode())
                .le(TradeOrder::getAutoPraise, LocalDateTime.now())
                .last("limit 1000")
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(tradeOrder -> {
            Long orderId = tradeOrder.getId();
            try {
                tradeOrderService.expireAutoPraise(orderId);
                log.info("订单已收货到期自动五星好评成功，订单ID：{}", orderId);
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("订单已收货到期自动五星好评失败，订单ID：{}，错误信息：{}", orderId, e.getMessage());
            }
        });
    }

}
