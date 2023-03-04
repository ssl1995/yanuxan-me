package com.msb.pay.channel.context;

import com.msb.framework.common.utils.SpringContextUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.enums.MchCodeEnum;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.constant.PayCenterConst;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支付通道上下文
 */
@Slf4j
public class ChannelContext {

    private static final Map<String, IPaymentService> paymentMap = new ConcurrentHashMap<>(MchCodeEnum.values().length);
    private static final Map<String, IPayCodeService> payCodeMap = new ConcurrentHashMap<>(PayCodeEnum.values().length);

    private ChannelContext() {

    }

    /**
     * 获取支付通道
     *
     * @param mchCode：商户代号
     * @return com.msb.pay.channel.IPaymentService
     * @author peng.xy
     * @date 2022/7/4
     */
    public static IPaymentService getIPaymentService(String mchCode) {
        log.info("获取支付通道：{}", mchCode);
        String key = PayCenterConst.PAYMENT_CODE.PREFIX + mchCode;
        IPaymentService paymentService = paymentMap.get(key);
        if (Objects.nonNull(paymentService)) {
            log.info("支付通道已缓存：{}", mchCode);
            return paymentService;
        }
        if (!paymentMap.containsKey(key)) {
            synchronized (key.intern()) {
                if (!paymentMap.containsKey(key)) {
                    paymentService = SpringContextUtil.getBean(key, IPaymentService.class);
                    BizAssert.notNull(paymentService, "获取支付通道失败");
                    paymentMap.put(key, paymentService);
                    log.info("从Spring容器获取支付通道：{}", mchCode);
                }
            }
        }
        return paymentMap.get(key);
    }


    /**
     * 获取支付方式
     *
     * @param payCode：支付代号
     * @return com.msb.pay.channel.IPaymentService
     * @author peng.xy
     * @date 2022/7/4
     */
    public static IPayCodeService getPayCodeService(String payCode) {
        log.info("获取支付方式：{}", payCode);
        String key = PayCenterConst.PAY_CODE.PREFIX + payCode;
        IPayCodeService payCodeService = payCodeMap.get(key);
        if (Objects.nonNull(payCodeService)) {
            log.info("支付方式已缓存：{}", payCode);
            return payCodeService;
        }
        if (!payCodeMap.containsKey(key)) {
            synchronized (key.intern()) {
                if (!payCodeMap.containsKey(key)) {
                    payCodeService = SpringContextUtil.getBean(key, IPayCodeService.class);
                    BizAssert.notNull(payCodeService, "获取支付方式失败");
                    payCodeMap.put(key, payCodeService);
                    log.info("从Spring容器获取支付方式：{}", payCode);
                }
            }
        }
        return payCodeMap.get(key);
    }
}
