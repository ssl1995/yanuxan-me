package com.msb.mall.trade.third.logistics.config;

import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.utils.SpringContextUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.trade.third.logistics.LogisticsApiClient;
import com.msb.mall.trade.third.logistics.LogisticsApiConfigEnum;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 快递API服务配置
 *
 * @author peng.xy
 * @date 2022/4/2
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "logistics-api")
public class LogisticsApiConfig {

    /**
     * 开启使用的API
     */
    private String enable;

    /**
     * API配置
     */
    private List<ApiConfig> configs;

    @Data
    public static class ApiConfig {

        /**
         * 服务商ID
         */
        private String id;

        /**
         * 签名key
         */
        private String key;

        /**
         * customer
         */
        private String customer;

        /**
         * 秘钥
         */
        private String secret;

        /**
         * 用户ID
         */
        private String userid;

        /**
         * 订单订阅回调地址
         */
        private String orderPollUrl;

        /**
         * 退款单订阅回调地址
         */
        private String refundPollUrl;
    }

    @Bean("logisticsApiClient")
    public LogisticsApiClient enableApiClient() throws IllegalAccessException, InstantiationException {
        if (StringUtils.isNotBlank(this.enable)) {
            LogisticsApiConfigEnum[] values = LogisticsApiConfigEnum.values();
            for (LogisticsApiConfigEnum apiConfigEnum : values) {
                if (Objects.equals(this.enable, apiConfigEnum.getCode())) {
                    LogisticsApiClient apiClient = apiConfigEnum.getApiClient().newInstance();
                    apiClient.initApiConfig(this.getApiById(apiConfigEnum.getCode()));
                    return apiClient;
                }
            }
        }
        throw new RuntimeException("获取快递API服务配置失败");
    }

    public ApiConfig getApiById(String id) {
        BizAssert.isTrue(CollectionUtils.isNotEmpty(configs), "暂无可用快递API服务配置");
        return configs.stream().filter(apiConfig -> Objects.equals(id, apiConfig.id)).findFirst().orElseThrow(() -> new BizException("快递API服务配置异常"));
    }

    public LogisticsApiClient getApiClientById(String id) {
        BizAssert.isTrue(CollectionUtils.isNotEmpty(configs), "暂无可用快递API服务配置");
        LogisticsApiConfigEnum apiConfigEnum = Arrays.stream(LogisticsApiConfigEnum.values()).filter(configEnum -> Objects.equals(id, configEnum.getCode())).findFirst().orElseThrow(() -> new BizException("快递API服务配置异常"));
        Class<? extends LogisticsApiClient> apiClient = apiConfigEnum.getApiClient();
        String simpleName = apiClient.getSimpleName();
        String beanName = simpleName.substring(0, 1).toLowerCase().concat(simpleName.substring(1));
        return SpringContextUtil.getBean(beanName, apiClient);
    }

}
