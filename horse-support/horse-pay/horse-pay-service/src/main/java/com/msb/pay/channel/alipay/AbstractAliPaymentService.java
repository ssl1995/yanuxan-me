package com.msb.pay.channel.alipay;

import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.channel.AbstractPaymentService;
import com.msb.pay.channel.alipay.config.AliPayConfig;
import com.msb.pay.channel.alipay.data.AliAppDataInfo;
import com.msb.pay.channel.alipay.data.AliMchDataInfo;
import com.msb.pay.enums.MchCodeEnum;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 支付宝支付对接抽象类
 *
 * @author peng.xy
 * @date 2022/6/1
 */
@Slf4j
public abstract class AbstractAliPaymentService extends AbstractPaymentService<AliMchDataInfo, AliAppDataInfo> {

    @Resource
    protected AliPayConfig aliPayConfig;

    @Override
    protected Class<AliMchDataInfo> mchDataClass() {
        return AliMchDataInfo.class;
    }

    @Override
    protected Class<AliAppDataInfo> appDataClass() {
        return AliAppDataInfo.class;
    }

    @Override
    protected MchCodeEnum mchCode() {
        return MchCodeEnum.ALIPAY;
    }

    @Override
    protected String getPayNotifyUrl(String payOrderNo) {
        return aliPayConfig.getPayNotify() + payOrderNo;
    }

    /**
     * 支付应用初始化
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @author peng.xy
     * @date 2022/6/21
     */
    protected AliPayApiConfig apiConfigInit(MchInfo mchInfo, AppInfo appInfo) {
        AliMchDataInfo aliMchDataInfo = this.getMchDataInfo(mchInfo);
        AliAppDataInfo aliAppDataInfo = this.getAppDataInfo(appInfo);
        return this.apiConfigInit(mchInfo, aliMchDataInfo, appInfo, aliAppDataInfo);
    }

    /**
     * 支付应用初始化
     *
     * @param mchInfo：商户信息
     * @param aliMchDataInfo：商户资料
     * @param appInfo：应用信息
     * @param aliAppDataInfo：应用资料
     * @author peng.xy
     * @date 2022/6/21
     */
    private AliPayApiConfig apiConfigInit(MchInfo mchInfo, AliMchDataInfo aliMchDataInfo, AppInfo appInfo, AliAppDataInfo aliAppDataInfo) {
        try {
            AliPayApiConfig payApiConfig = AliPayApiConfigKit.getApiConfig(appInfo.getAppId());
            AliPayApiConfigKit.setThreadLocalAliPayApiConfig(payApiConfig);
            return payApiConfig;
        } catch (IllegalStateException illegalStateException) {
            log.warn("获取应用配置信息失败，进行初始化");
            try {
                AliPayApiConfig config = AliPayApiConfig.builder()
                        .setAppId(appInfo.getAppId())
                        .setAliPayPublicKey(aliMchDataInfo.getPublicKey())
                        .setPrivateKey(aliAppDataInfo.getPrivateKey())
                        .setAppCertPath(aliAppDataInfo.getAppCertPath())
                        .setAliPayCertPath(aliMchDataInfo.getAliPayCertPath())
                        .setAliPayRootCertPath(aliMchDataInfo.getAliPayRootCertPath())
                        .setCharset("UTF-8")
                        .setServiceUrl("https://openapi.alipay.com/gateway.do")
                        .setSignType("RSA2");
                config.buildByCert();
                AliPayApiConfigKit.putApiConfig(config);
                return config;
            } catch (AlipayApiException e) {
                log.error("支付宝应用初始化失败", e);
                throw new BizException("支付宝应用初始化失败");
            }
        }
    }

}
