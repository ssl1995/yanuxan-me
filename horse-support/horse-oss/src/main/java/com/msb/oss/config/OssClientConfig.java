package com.msb.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class OssClientConfig {

    @Resource
    private OssConfig ossConfig;

    @Bean(name = "ossClient")
    public OSS initOssClient() {
        return new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKey(), ossConfig.getAccessKeySecret());
    }
}
