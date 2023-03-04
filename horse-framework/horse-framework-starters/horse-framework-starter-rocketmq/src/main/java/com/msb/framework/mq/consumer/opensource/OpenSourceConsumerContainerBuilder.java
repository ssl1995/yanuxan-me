package com.msb.framework.mq.consumer.opensource;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.mq.RocketMqProviderEnum;
import com.msb.framework.mq.annotation.Consumer;
import com.msb.framework.mq.consumer.ConsumerContainerBuilder;
import com.msb.framework.mq.exception.MqException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.StandardEnvironment;
import sun.reflect.annotation.AnnotationParser;
import sun.reflect.annotation.AnnotationType;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * @author liao
 */
public class OpenSourceConsumerContainerBuilder implements ConsumerContainerBuilder {

    private static final Logger log = LoggerFactory.getLogger(OpenSourceConsumerContainerBuilder.class);

    private final ConfigurableApplicationContext applicationContext;

    private final StandardEnvironment environment;

    private final RocketMQProperties rocketMQProperties;

    private final RocketMQMessageConverter rocketMQMessageConverter;

    public OpenSourceConsumerContainerBuilder(ConfigurableApplicationContext applicationContext, RocketMQMessageConverter rocketMqMessageConverter, StandardEnvironment environment, RocketMQProperties rocketMqProperties) {
        this.applicationContext = applicationContext;
        this.rocketMQMessageConverter = rocketMqMessageConverter;
        this.environment = environment;
        this.rocketMQProperties = rocketMqProperties;
    }

    private RocketMQMessageListener instanceRocketMessageListener(String topic, String consumerGroup, String tag, ConsumeMode consumeMode, MessageModel messageModel) {
        //获取注解默认属性
        AnnotationType instance = AnnotationType.getInstance(RocketMQMessageListener.class);
        Map<String, Object> stringObjectMap = instance.memberDefaults();
        // 完善注解必填属性
        stringObjectMap.put("topic", topic);
        stringObjectMap.put("consumerGroup", consumerGroup);
        stringObjectMap.put("selectorExpression", tag);
        stringObjectMap.put("consumeMode", consumeMode);
        stringObjectMap.put("messageModel", messageModel);
        //反射生成实例
        return (RocketMQMessageListener) AnnotationParser.annotationForMap(RocketMQMessageListener.class, stringObjectMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doRegisterContainer(Class<?> targetClass, String beanName, Object bean, String topic, String groupId, Consumer consumer) {

        String tag = consumer.tag();
        ConsumeMode consumeMode = consumer.consumeMode();
        MessageModel messageModel = consumer.messageModel();

        RocketMQMessageListener annotation = instanceRocketMessageListener(topic, groupId, tag, consumeMode, messageModel);

        boolean listenerEnabled = rocketMQProperties.getConsumer().getListeners().getOrDefault(groupId, Collections.emptyMap()).getOrDefault(topic, true);

        if (!listenerEnabled) {
            log.debug("Consumer Listener (group:{},topic:{}) is not enabled by configuration, will ignore initialization.", groupId, topic);
            return;
        }
        validate(annotation);

        String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(), UUID.randomUUID());
        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;
        MqTopic<Object> messageContext = (MqTopic<Object>) bean;
        RocketMQListener<Object> rocketListener = messageContext::consumer;


        genericApplicationContext.registerBean(containerBeanName, DefaultRocketMQListenerContainer.class, () -> createRocketMqListenerContainer(containerBeanName, rocketListener, annotation));
        DefaultRocketMQListenerContainer container = genericApplicationContext.getBean(containerBeanName, DefaultRocketMQListenerContainer.class);
        if (!container.isRunning()) {
            try {
                ResolvableType resolvableType = ResolvableType.forClass(targetClass);
                ResolvableType generic = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0);
                Type type = generic.getType();

                Field messageType = container.getClass().getDeclaredField("messageType");
                messageType.setAccessible(true);
                messageType.set(container, type);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new MqException(e);
            }
            container.start();
        }

        log.info("Register the listener to container, listenerBeanName:{}, containerBeanName:{}", beanName, containerBeanName);
    }

    @Override
    public RocketMqProviderEnum rocketMqProvider() {
        return RocketMqProviderEnum.OPEN_SOURCE;
    }

    private DefaultRocketMQListenerContainer createRocketMqListenerContainer(String name, Object bean, RocketMQMessageListener annotation) {
        DefaultRocketMQListenerContainer container = new DefaultRocketMQListenerContainer();
        container.setRocketMQMessageListener(annotation);

        String nameServer = environment.resolvePlaceholders(annotation.nameServer());
        nameServer = org.apache.commons.lang3.StringUtils.isEmpty(nameServer) ? rocketMQProperties.getNameServer() : nameServer;
        String accessChannel = environment.resolvePlaceholders(annotation.accessChannel());
        container.setNameServer(nameServer);
        if (!StringUtils.isEmpty(accessChannel)) {
            container.setAccessChannel(AccessChannel.valueOf(accessChannel));
        }
        container.setTopic(environment.resolvePlaceholders(annotation.topic()));
        String tags = environment.resolvePlaceholders(annotation.selectorExpression());
        if (!StringUtils.isEmpty(tags)) {
            container.setSelectorExpression(tags);
        }
        container.setConsumerGroup(environment.resolvePlaceholders(annotation.consumerGroup()));
        //老版本没有这个字段，暂时先注释掉
        if (RocketMQListener.class.isAssignableFrom(bean.getClass())) {
            container.setRocketMQListener((RocketMQListener<?>) bean);
        } else if (RocketMQReplyListener.class.isAssignableFrom(bean.getClass())) {
            container.setRocketMQReplyListener((RocketMQReplyListener<?, ?>) bean);
        }
        container.setMessageConverter(rocketMQMessageConverter.getMessageConverter());
        container.setName(name);

        return container;
    }

    private void validate(RocketMQMessageListener annotation) {
        if (annotation.consumeMode() == ConsumeMode.ORDERLY &&
                annotation.messageModel() == MessageModel.BROADCASTING) {
            throw new BeanDefinitionValidationException(
                    "Bad annotation definition in @RocketMQMessageListener, messageModel BROADCASTING does not support ORDERLY message!");
        }
    }


}
