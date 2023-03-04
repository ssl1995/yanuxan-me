package com.msb.pay.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss")
public class OssConfig {

    private String endpoint;

    private String accessKey;

    private String accessKeySecret;

    private String bucket;

    private String dir;

    private Long expire;

    @Bean(name = "ossClient")
    public OSS initOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
    }

    public String getHost() {
        return "https://".concat(bucket).concat(".").concat(endpoint);
    }

}
