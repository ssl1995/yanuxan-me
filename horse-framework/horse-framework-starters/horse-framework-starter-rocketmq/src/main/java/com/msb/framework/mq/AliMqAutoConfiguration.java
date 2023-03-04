package com.msb.framework.mq;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.msb.framework.mq.config.RocketMqConfigurationProperties;
import com.msb.framework.mq.consumer.ali.AliMqConsumerContainerBuilder;
import com.msb.framework.mq.producer.alimq.AliRocketMqProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author liao
 */


@ConditionalOnProperty(name = "rocketmq.server-provider", havingValue = "ali_cloud")
@Import({AliMqConsumerContainerBuilder.class, AliRocketMqProducer.class})
@Configuration
public class AliMqAutoConfiguration {
    @Resource
    private RocketMqConfigurationProperties rocketMqConfiguration;

    @Resource
    private RocketMQProperties rocketMqProperties;

    public Properties initAliMqProperties() {
        RocketMqConfigurationProperties.AliMqConfiguration aliMqConfiguration = rocketMqConfiguration.getAliMq();
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, aliMqConfiguration.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, aliMqConfiguration.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, rocketMqProperties.getNameServer());
        return properties;
    }


    @Bean
    public Producer producer() {
        Properties properties = initAliMqProperties();
        Producer producer = ONSFactory.createProducer(properties);
        producer.start();
        return producer;
    }

    @Bean
    public OrderProducer orderProducer() {
        Properties properties = initAliMqProperties();
        OrderProducer orderProducer = ONSFactory.createOrderProducer(properties);
        orderProducer.start();
        return orderProducer;
    }
}
