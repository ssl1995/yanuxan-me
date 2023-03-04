package com.msb.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "oss")
@Component
public class OssConfig {

    private String endpoint;

    private String accessKey;

    private String accessKeySecret;

    private String bucket;

    private String callback;

    private String host;

    private Long expire;


    public String getHost() {
        return "https://".concat(bucket).concat(".").concat(endpoint);
    }
}
