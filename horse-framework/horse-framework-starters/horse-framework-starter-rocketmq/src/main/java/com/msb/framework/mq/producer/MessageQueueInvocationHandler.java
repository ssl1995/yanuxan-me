package com.msb.framework.mq.producer;

import com.msb.framework.common.mq.MqConfig;
import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.framework.common.utils.SpringContextUtil;
import com.msb.framework.mq.RocketMqProviderEnum;
import com.msb.framework.mq.config.RocketMqConfigurationProperties;
import com.msb.framework.mq.exception.MqException;
import com.msb.framework.mq.producer.alimq.AliRocketMqProducer;
import com.msb.framework.mq.producer.opensource.OpenSourceRocketMqProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

import java.lang.reflect.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liao
 */
@Slf4j
public class MessageQueueInvocationHandler extends RocketMQTemplate implements InvocationHandler {

    /**
     * 缓存topic 对应的注解
     */
    private static final Map<Class<?>, Topic> MESSAGE_QUEUE_MAP = new ConcurrentHashMap<>();

    private MessageQueueProducerStrategy messageQueueProducerStrategy;

    private static final Method PRODUCT_01;

    private static final Method PRODUCT_02;

    static {
        try {
            PRODUCT_01 = MqTopic.class.getMethod("product", Object.class);
            PRODUCT_02 = MqTopic.class.getMethod("product", MqConfig.class, Object.class);
        } catch (NoSuchMethodException e) {
            throw new MqException("mq 生产函数加载失败");
        }
    }


    public static Object generateProxyClass(Class<?> type) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new MessageQueueInvocationHandler());
    }

    private MessageQueueProducerStrategy getMessageQueueProducerStrategy() {
        if (messageQueueProducerStrategy != null) {
            return messageQueueProducerStrategy;
        }
        RocketMqConfigurationProperties rocketMqConfigurationProperties = SpringContextUtil.getBean(RocketMqConfigurationProperties.class);
        RocketMqProviderEnum rocketMqProvider = rocketMqConfigurationProperties.getServerProvider();
        switch (rocketMqProvider) {
            case ALI_CLOUD:
                messageQueueProducerStrategy = SpringContextUtil.getBean(AliRocketMqProducer.class);
                break;
            case OPEN_SOURCE:
                messageQueueProducerStrategy = SpringContextUtil.getBean(OpenSourceRocketMqProducer.class);
                break;
            default:
                throw new MqException("暂不支持此mq服务");
        }
        return messageQueueProducerStrategy;
    }

    private Topic getMessageQueue(Object proxy) {
        Topic messageQueue;
        if ((messageQueue = MESSAGE_QUEUE_MAP.get(proxy.getClass())) != null) {
            return messageQueue;
        }
        Type[] genericInterfaces = proxy.getClass().getGenericInterfaces();
        Class<?> genericInterface = (Class<?>) genericInterfaces[0];
        Topic annotation = genericInterface.getAnnotation(Topic.class);
        MESSAGE_QUEUE_MAP.put(proxy.getClass(), annotation);
        return annotation;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }

        MqConfig mqConfig = null;
        Object body;

        if (args.length == 2) {
            mqConfig = (MqConfig) args[0];
            body = args[1];
        } else if (args.length == 1) {
            body = args[0];
        } else {
            log.warn("mq 发送失败，代理方法有问题");
            return null;
        }

        Topic messageQueue = getMessageQueue(proxy);

        String topic = SpringContextUtil.getSpringValue(messageQueue.value());


        MessageQueueProducerStrategy messageQueueProducer = getMessageQueueProducerStrategy();
        messageQueueProducer.producer(topic, mqConfig, body);
        return null;
    }

}
