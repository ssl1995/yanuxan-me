package com.msb.im.mq.consumer;

import com.msb.framework.mq.annotation.Consumer;
import com.msb.framework.redis.RedisClient;
import com.msb.im.mq.model.ImMqMessage;
import com.msb.im.mq.model.ImMqMessageTypeEnum;
import com.msb.im.mq.service.AbstractImMqMessageHandleService;
import com.msb.im.mq.topic.ImMessageTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.concurrent.TimeUnit;

import static com.msb.im.redis.RedisConstant.MQ_MESSAGE_CONSUMED;
import static com.msb.im.redis.RedisConstant.MQ_MESSAGE_CONSUMED_EXPIRE_DAYS;

/**
 * @author zhou miao
 * @date 2022/04/26
 */
@Consumer
@Component
@Slf4j
public class ImMessageConsumer implements ImMessageTopic {
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private RedisClient redisClient;

    @Override
    public void consumer(ImMqMessage mqMessage) {
        try {
            log.info("消费mq {}", mqMessage);
            String uuid = mqMessage.getUuid();
            String key = MQ_MESSAGE_CONSUMED + uuid;
            if (redisClient.exists(key)) {
                return;
            }
            ImMqMessageTypeEnum imMqMessageTypeEnum = mqMessage.getImMqMessageTypeEnum();
            Class<?> handleService = imMqMessageTypeEnum.getHandleService();
            AbstractImMqMessageHandleService abstractImMqMessageHandleService = (AbstractImMqMessageHandleService) applicationContext.getBean(handleService);
            abstractImMqMessageHandleService.handle2Db(mqMessage);

            redisClient.set(key, "", MQ_MESSAGE_CONSUMED_EXPIRE_DAYS, TimeUnit.DAYS);
        } catch (Exception e) {
            log.error("消费错误 {}", e.getMessage());
            // todo 需要告警
        }
    }

}
