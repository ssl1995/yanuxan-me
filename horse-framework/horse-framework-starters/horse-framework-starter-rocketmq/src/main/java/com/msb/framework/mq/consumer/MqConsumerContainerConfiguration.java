package com.msb.framework.mq.consumer;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.framework.mq.RocketMqProviderEnum;
import com.msb.framework.mq.annotation.Consumer;
import com.msb.framework.mq.config.RocketMqConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.lang.NonNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liao
 */
@Slf4j
@Configuration
public class MqConsumerContainerConfiguration implements ApplicationContextAware, SmartInitializingSingleton {

    private ConfigurableApplicationContext applicationContext;

    private final StandardEnvironment environment;

    private final RocketMqConfigurationProperties rocketMqConfigurationProperties;

    private final ConsumerContainerBuilder consumerContainerBuilder;

    public MqConsumerContainerConfiguration(StandardEnvironment environment, RocketMqConfigurationProperties rocketMqConfigurationProperties, List<ConsumerContainerBuilder> consumerContainerBuilder) {
        Map<RocketMqProviderEnum, ConsumerContainerBuilder> consumerContainerBuilderMap = consumerContainerBuilder.stream()
                .collect(Collectors.toMap(ConsumerContainerBuilder::rocketMqProvider, Function.identity()));
        this.consumerContainerBuilder = consumerContainerBuilderMap.get(rocketMqConfigurationProperties.getServerProvider());
        this.environment = environment;
        this.rocketMqConfigurationProperties = rocketMqConfigurationProperties;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        RocketMqProviderEnum serverProvider = rocketMqConfigurationProperties.getServerProvider();
        if (serverProvider == null) {
            log.warn("no config the specified rocketmq provider");
            return;
        }
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(Consumer.class).entrySet().stream()
                .filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        beans.forEach(this::buildConsumerContainer);
    }

    private void buildConsumerContainer(String beanName, Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (!MqTopic.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + MqTopic.class.getName());
        }

        Consumer consumerGroupAnnotation = clazz.getAnnotation(Consumer.class);

        Type genericInterface = clazz.getGenericInterfaces()[0];
        Class<?> parameterizedType = (Class<?>) genericInterface;
        Topic messageQueueAnnotation = parameterizedType.getAnnotation(Topic.class);

        String topic = this.environment.resolvePlaceholders(messageQueueAnnotation.value());

        if (consumerContainerBuilder == null) {
            log.warn("消费者未启动");
            return;
        }
        consumerContainerBuilder.registerContainer(clazz, beanName, bean, topic, consumerGroupAnnotation);
    }
}
