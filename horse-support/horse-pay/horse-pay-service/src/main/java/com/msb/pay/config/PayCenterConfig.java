package com.msb.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 支付中台配置信息
 *
 * @author peng.xy
 * @date 2022/7/6
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "pay-center")
public class PayCenterConfig {

    /**
     * 预支付微信应用代号
     */
    private String prepayWxAppCode;

    /**
     * 预支付支付宝应用代号
     */
    private String prepayAliAppCode;

    /**
     * 手机收银台页面
     */
    private String wapCashierUrl;

    /**
     * 支付结果页
     */
    private String wapResultUrl;

}
