package com.msb.mall.trade.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.Assert;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.mapper.TradeOrderPayCenterMapper;
import com.msb.mall.trade.model.entity.TradeOrderPayCenter;
import com.msb.mall.trade.service.convert.TradeOrderPayCenterConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 交易订单支付对接(TradeOrderPayCenter)表服务实现类
 *
 * @author makejava
 * @date 2022-04-18 16:12:39
 */
@Service("tradeOrderPayCenterService")
public class TradeOrderPayCenterService extends ServiceImpl<TradeOrderPayCenterMapper, TradeOrderPayCenter> {

    @Resource
    private TradeOrderPayCenterConvert tradeOrderPayCenterConvert;

    /**
     * 根据商户订单号获取支付信息，获取不到则抛出异常
     *
     * @param payOrderNo：支付订单号
     * @return com.msb.mall.trade.model.entity.TradeOrderPayCenter
     * @author peng.xy
     * @date 2022/4/18
     */
    public TradeOrderPayCenter getByPayOrderNoOrThrow(@Nonnull String payOrderNo) {
        TradeOrderPayCenter tradeOrderPayCenter = super.lambdaQuery().eq(TradeOrderPayCenter::getPayOrderNo, payOrderNo).one();
        Assert.notNull(tradeOrderPayCenter, TradeExceptionCodeEnum.ORDER_PAY_DATA_EXCEPTION);
        return tradeOrderPayCenter;
    }

    /**
     * 根据支付订单ID获取支付信息，获取不到则抛出异常
     *
     * @param payOrderId：JeePay支付订单号
     * @return com.msb.mall.trade.model.entity.TradeOrderPayCenter
     * @author peng.xy
     * @date 2022/4/18
     */
    public TradeOrderPayCenter getByPayOrderIdOrThrow(@Nonnull String payOrderId) {
        TradeOrderPayCenter tradeOrderPayCenter = super.lambdaQuery().eq(TradeOrderPayCenter::getPayOrderId, payOrderId).one();
        Assert.notNull(tradeOrderPayCenter, TradeExceptionCodeEnum.ORDER_PAY_DATA_EXCEPTION);
        return tradeOrderPayCenter;
    }

    /**
     * 更新状态为支付成功
     *
     * @param tradeOrderPayCenterId：支付信息ID
     * @param appCode：支付应用代号
     * @param payCode：支付方式
     * @return boolean
     * @author peng.xy
     * @date 2022/4/18
     */
    public boolean paySuccess(@Nonnull Long tradeOrderPayCenterId, String appCode, String payCode) {
        boolean update = super.lambdaUpdate().eq(TradeOrderPayCenter::getId, tradeOrderPayCenterId)
                .set(TradeOrderPayCenter::getAppCode, appCode)
                .set(TradeOrderPayCenter::getPayCode, payCode)
                .set(TradeOrderPayCenter::getIsSuccess, CommonConst.YES)
                .set(TradeOrderPayCenter::getUpdateTime, LocalDateTime.now())
                .set(TradeOrderPayCenter::getUpdateUser, UserContext.getUserIdOrDefault())
                .update();
        Assert.isTrue(update, TradeExceptionCodeEnum.ORDER_PAY_UPDATE_FAIL);
        return true;
    }

    /**
     * 获取支付成功的订单支付信息
     *
     * @param orderId：订单ID
     * @return com.msb.mall.trade.model.entity.TradeOrderPayCenter
     * @author peng.xy
     * @date 2022/4/19
     */
    public TradeOrderPayCenter getPaySuccessByOrderIdOrThrow(@Nonnull Long orderId) {
        return super.lambdaQuery().eq(TradeOrderPayCenter::getOrderId, orderId)
                .eq(TradeOrderPayCenter::getIsSuccess, CommonConst.YES)
                .list().stream().findFirst().orElseThrow(() -> new BizException(TradeExceptionCodeEnum.ORDER_PAY_DATA_EXCEPTION));
    }

}

