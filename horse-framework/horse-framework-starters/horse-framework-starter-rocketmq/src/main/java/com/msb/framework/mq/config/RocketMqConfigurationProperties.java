package com.msb.framework.mq.config;

import com.msb.framework.mq.RocketMqProviderEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liao
 */
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMqConfigurationProperties {

    private RocketMqProviderEnum serverProvider;

    private AliMqConfiguration aliMq;

    public static class AliMqConfiguration {
        private String accessKey;
        private String secretKey;
        private String groupId;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }

    public RocketMqProviderEnum getServerProvider() {
        return serverProvider;
    }

    public void setServerProvider(RocketMqProviderEnum serverProvider) {
        this.serverProvider = serverProvider;
    }

    public AliMqConfiguration getAliMq() {
        return aliMq;
    }

    public void setAliMq(AliMqConfiguration aliMq) {
        this.aliMq = aliMq;
    }
}
