package com.msb.push.config.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class AliyunSmsConfig {

    @Resource
    private SmsConfiguration smsConfiguration;

    @Bean
    public Client initSmsConfig() throws Exception {
        Config config = new Config()
                .setAccessKeyId(smsConfiguration.getAccessKeyId())
                .setAccessKeySecret(smsConfiguration.getSecret())
                .setEndpoint(smsConfiguration.getDomain());
        return new Client(config);
    }
}
