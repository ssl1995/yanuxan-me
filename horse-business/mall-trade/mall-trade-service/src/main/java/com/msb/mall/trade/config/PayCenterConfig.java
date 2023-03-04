package com.msb.mall.trade.config;

import com.msb.framework.common.exception.BizException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "pay-center")
public class PayCenterConfig {

    private String payNotifyUrl;

    private String refundNotifyUrl;

    private List<PayApp> payApps;

    /**
     * 根据支付方式获取支付应用
     *
     * @param payCode：支付方式
     * @return com.msb.mall.trade.config.PayCenterConfig.AppConfig
     * @author peng.xy
     * @date 2022/6/13
     */
    public PayApp getByPayCode(String payCode) {
        return payApps.stream().filter(payApp -> payApp.hasPayCode(payCode)).findFirst().orElseThrow(() -> new BizException("获取支付应用失败"));
    }

    /**
     * 根据应用代号获取支付应用
     *
     * @param appCode：应用代号
     * @return com.msb.mall.trade.config.PayCenterConfig.AppConfig
     * @author peng.xy
     * @date 2022/6/13
     */
    public PayApp getByAppCode(String appCode) {
        return payApps.stream().filter(payApp -> StringUtils.equals(payApp.getAppCode(), appCode)).findFirst().orElseThrow(() -> new BizException("获取支付应用失败"));
    }

    @Data
    public static class PayApp {
        private String appCode;
        private String signKey;
        private List<String> payCodes = new ArrayList<>();

        private boolean hasPayCode(String payCode) {
            return payCodes.contains(payCode);
        }
    }

}
