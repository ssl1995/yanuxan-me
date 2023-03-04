package com.msb.framework.mq.annotation;


import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface Consumer {
    String groupId() default "${spring.application.name}";

    String tag() default "*";

    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

    MessageModel messageModel() default MessageModel.CLUSTERING;
}
