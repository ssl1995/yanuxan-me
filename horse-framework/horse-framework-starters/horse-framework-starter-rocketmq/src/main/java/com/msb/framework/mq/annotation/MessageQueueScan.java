package com.msb.framework.mq.annotation;


import com.msb.framework.mq.spring.MessageQueueRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MessageQueueRegistrar.class)
public @interface MessageQueueScan {

    String [] value();
}
