package com.msb.framework.mq.consumer.ali;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.mq.AliMqAutoConfiguration;
import com.msb.framework.mq.RocketMqProviderEnum;
import com.msb.framework.mq.annotation.Consumer;
import com.msb.framework.mq.consumer.ConsumerContainerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.core.ResolvableType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author liao
 */
@Slf4j
public class AliMqConsumerContainerBuilder implements ConsumerContainerBuilder {

    private final ObjectMapper objectMapper;

    private final AliMqAutoConfiguration aliMqProducerConfiguration;


    public AliMqConsumerContainerBuilder(AliMqAutoConfiguration aliMqProducerConfiguration, JavaTimeModule javaTimeModule) {
        this.aliMqProducerConfiguration = aliMqProducerConfiguration;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
    }


    @Override
    public void doRegisterContainer(Class<?> targetClass, String beanName, Object bean, String topic, String groupId, Consumer consumer) {

        String tag = consumer.tag();

        Properties properties = aliMqProducerConfiguration.initAliMqProperties();
        //????????????
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        //??????????????????????????????20??? 20????????????
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");

        switch (consumer.messageModel()) {
            case CLUSTERING:
                properties.setProperty(PropertyKeyConst.MessageModel, MessageModel.CLUSTERING.getModeCN());
                break;
            case BROADCASTING:
                properties.setProperty(PropertyKeyConst.MessageModel, MessageModel.BROADCASTING.getModeCN());
                break;
            default:
                break;
        }

        @SuppressWarnings("unchecked")
        MqTopic<Object> mqTopic = (MqTopic<Object>) bean;

        ResolvableType resolvableType = ResolvableType.forClass(targetClass);
        ResolvableType generic = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0);
        Class<?> messageType = generic.getRawClass();

        //????????????
        Subscription subscription = new Subscription();
        subscription.setTopic(topic);
        subscription.setExpression(tag);
        //????????????topic???????????????

        ConsumeMode consumeMode = consumer.consumeMode();
        switch (consumeMode) {
            case CONCURRENTLY:
                consumer(mqTopic, messageType, subscription, properties);
                break;
            case ORDERLY:
                orderConsumer(mqTopic, messageType, subscription, properties);
                break;
            default:
                break;
        }

        String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(), UUID.randomUUID());
        log.info("Register the listener to container, listenerBeanName:{}, containerBeanName:{}, topic: {}, groupId: {}", beanName, containerBeanName, topic, groupId);
    }

    private void consumer(MqTopic<Object> mqTopic, Class<?> messageType, Subscription subscription, Properties properties) {
        MessageListener messageListener = (message, context) -> {
            log.info("consumer {}", message);
            message.getBody();
            Object body;
            try {
                //???????????? ?????????????????????????????????
                body = objectMapper.readValue(message.getBody(), messageType);
            } catch (IOException e) {
                log.error("alimq ????????????, {}", message, e);
                return Action.CommitMessage;
            }
            try {
                mqTopic.consumer(body);
            } catch (Exception e) {
                return Action.ReconsumeLater;
            }

            return Action.CommitMessage;
        };

        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>(1);
        subscriptionTable.put(subscription, messageListener);
        ConsumerBean consumerBean = new ConsumerBean();
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        consumerBean.start();
    }

    private void orderConsumer(MqTopic<Object> mqTopic, Class<?> messageType, Subscription subscription, Properties properties) {
        MessageOrderListener messageListener = (message, context) -> {
            log.info("consumer {}", message);
            message.getBody();
            Object body;
            try {
                //???????????? ?????????????????????????????????
                body = objectMapper.readValue(message.getBody(), messageType);
            } catch (IOException e) {
                log.error("alimq ????????????, {}", message, e);
                return OrderAction.Success;
            }
            try {
                mqTopic.consumer(body);
            } catch (Exception e) {
                return OrderAction.Suspend;
            }

            return OrderAction.Success;
        };
        Map<Subscription, MessageOrderListener> subscriptionTable = new HashMap<>(1);
        subscriptionTable.put(subscription, messageListener);
        OrderConsumerBean consumerBean = new OrderConsumerBean();
        consumerBean.setProperties(properties);
        consumerBean.setSubscriptionTable(subscriptionTable);
        consumerBean.start();
    }

    @Override
    public RocketMqProviderEnum rocketMqProvider() {
        return RocketMqProviderEnum.ALI_CLOUD;
    }
}
