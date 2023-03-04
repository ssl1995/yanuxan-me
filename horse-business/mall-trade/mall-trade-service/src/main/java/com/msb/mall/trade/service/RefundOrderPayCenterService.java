package com.msb.mall.trade.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.RefundOrderPayCenterMapper;
import com.msb.mall.trade.model.entity.RefundOrderPayCenter;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * 退款单支付中台对接(RefundOrderPayCenter)表服务实现类
 *
 * @author makejava
 * @date 2022-04-19 14:10:50
 */
@Service("refundOrderPayCenterService")
public class RefundOrderPayCenterService extends ServiceImpl<RefundOrderPayCenterMapper, RefundOrderPayCenter> {

    /**
     * 根据商户退款单号获取退款请求信息，获取不到则抛出异常
     *
     * @param refundOrderNo：退款订单号
     * @return com.msb.mall.trade.model.entity.RefundOrderPayCenter
     * @author peng.xy
     * @date 2022/4/19
     */
    public RefundOrderPayCenter getByRefundOrderNoOrThrow(@Nonnull String refundOrderNo) {
        RefundOrderPayCenter refundOrderPayCenter = super.lambdaQuery().eq(RefundOrderPayCenter::getRefundOrderNo, refundOrderNo).one();
        Assert.notNull(refundOrderPayCenter, TradeExceptionCodeEnum.REFUND_REQUEST_DATA_EXCEPTION);
        return refundOrderPayCenter;
    }

    /**
     * 更新状态为退款成功
     *
     * @param refundOrderPayCenterId：退款请求ID
     * @return boolean
     * @author peng.xy
     * @date 2022/4/19
     */
    public boolean refundSuccess(Long refundOrderPayCenterId) {
        boolean update = super.lambdaUpdate().eq(RefundOrderPayCenter::getId, refundOrderPayCenterId)
                .set(RefundOrderPayCenter::getIsSuccess, CommonConst.YES)
                .set(RefundOrderPayCenter::getUpdateTime, LocalDateTime.now())
                .set(RefundOrderPayCenter::getUpdateUser, UserContext.getUserIdOrDefault())
                .update();
        Assert.isTrue(update, TradeExceptionCodeEnum.REFUND_REQUEST_UPDATE_FAIL);
        return true;
    }

}

