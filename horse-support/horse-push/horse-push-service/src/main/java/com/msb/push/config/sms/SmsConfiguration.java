package com.msb.push.config.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfiguration {

    private String regionId;

    private String internationalRegionId;

    private String accessKeyId;

    private String secret;

    private String signName;

    private String domain;

    private Map<String, Template> templateMap;

    @Data
    public static class Template {
        private String templateCode;
        private List<String> paramNameList;
    }
}
