package com.msb.pay.channel.alipay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝支付配置，暂时将resource目录下的证书文件存储到硬盘下，待后期实现从oss下载证书
 *
 * @author peng.xy
 * @date 2022/6/20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {

    /**
     * 支付中台支付回调
     */
    private String payNotify;

    /**
     * 支付中台退款回调
     */
    private String refundNotify;

}
