package com.msb.framework.mq;


import com.msb.framework.mq.annotation.MessageQueueScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author liao
 */
@Configuration
@MessageQueueScan("com.msb.**.mq.**")
@EnableConfigurationProperties
@ConfigurationPropertiesScan("com.msb.framework.mq.config")
@ComponentScan("com.msb.framework.mq.*")
public class MessageQueueAutoConfiguration {

}
