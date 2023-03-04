package com.msb.mall.trade.job;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.trade.enums.RefundStatusEnum;
import com.msb.mall.trade.model.entity.RefundOrder;
import com.msb.mall.trade.model.vo.app.LogisticsExpressQueryVO;
import com.msb.mall.trade.service.RefundOrderLogisticsService;
import com.msb.mall.trade.service.RefundOrderService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款单定时任务
 */
@Slf4j
@Component
public class RefundOrderJob {

    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private RefundOrderLogisticsService refundOrderLogisticsService;

    /**
     * 售后申请超时自动处理任务
     *
     * @author peng.xy
     * @date 2022/4/15
     */
    @XxlJob("refundHandleExpireTask")
    public void refundHandleExpireTask() {
        List<RefundOrder> list = refundOrderService.lambdaQuery()
                .select(RefundOrder::getId)
                .eq(RefundOrder::getRefundStatus, RefundStatusEnum.APPLY.getCode())
                .le(RefundOrder::getHandleExpireTime, LocalDateTime.now())
                .last("limit 1000")
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(refundOrder -> {
            Long refundId = refundOrder.getId();
            try {
                refundOrderService.expireHandle(refundId);
                log.info("退款单超时自动处理成功，退款单ID：{}", refundId);
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("退款单超时自动处理失败，退款单ID：{}，错误信息：{}", refundId, e.getMessage());
            }
        });
    }

    /**
     * 用户退货超时定时任务
     *
     * @author peng.xy
     * @date 2022/4/15
     */
    @XxlJob("refundReturnExpireTask")
    public void refundReturnExpireTask() {
        List<RefundOrder> list = refundOrderService.lambdaQuery()
                .select(RefundOrder::getId)
                .eq(RefundOrder::getRefundStatus, RefundStatusEnum.WAIT_RETURN.getCode())
                .le(RefundOrder::getReturnExpireTime, LocalDateTime.now())
                .last("limit 1000")
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(refundOrder -> {
            Long refundId = refundOrder.getId();
            try {
                refundOrderService.expireReturn(refundId);
                log.info("退货单超时未退货自动关闭成功，退款单ID：{}", refundId);
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("退货单超时未退货自动关闭失败，退款单ID：{}，错误信息：{}", refundId, e.getMessage());
            }
        });
    }

    /**
     * 商家收货超时定时任务
     *
     * @author peng.xy
     * @date 2022/4/15
     */
    @XxlJob("refundReceivingExpireTask")
    public void refundReceivingExpireTask() {
        List<RefundOrder> list = refundOrderService.lambdaQuery()
                .select(RefundOrder::getId)
                .eq(RefundOrder::getRefundStatus, RefundStatusEnum.IN_RETURN.getCode())
                .le(RefundOrder::getReceivingExpireTime, LocalDateTime.now())
                .last("limit 1000")
                .list();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(refundOrder -> {
            Long refundId = refundOrder.getId();
            try {
                refundOrderService.expireReceiving(refundId);
                log.info("退货单商家收货超时，自动确认成功，退款单ID：{}", refundId);
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("退货单商家收货超时，自动确认失败，退款单ID：{}，错误信息：{}", refundId, e.getMessage());
            }
        });
    }

    /**
     * 定时更新退款单物流任务
     *
     * @author peng.xy
     * @date 2022/4/7
     */
    @XxlJob("refundRefreshLogisticsTask")
    public void refundRefreshLogisticsTask() {
        this.refundRefreshLogistics(1, 1000);
    }

    private void refundRefreshLogistics(int pageIndex, int size) {
        Page<RefundOrder> page = refundOrderService.lambdaQuery()
                .select(RefundOrder::getId)
                .eq(RefundOrder::getRefundStatus, RefundStatusEnum.IN_RETURN.getCode())
                .page(new Page<>(pageIndex, size));
        List<RefundOrder> list = page.getRecords();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(refundOrder -> {
            Long refundId = refundOrder.getId();
            try {
                LogisticsExpressQueryVO expressQueryVO = refundOrderLogisticsService.refreshLogistics(refundId);
                log.info("定时更新退款单物流，订单ID：{}，更新结果：{}", refundId, expressQueryVO.getIsSuccess());
                Thread.sleep(10);
            } catch (Exception e) {
                log.error("定时更新订单物流失败，订单ID：{}，错误信息：{}", refundId, e.getMessage());
            }
        });
        if (page.hasNext()) {
            this.refundRefreshLogistics(++pageIndex, size);
        }
    }

}
