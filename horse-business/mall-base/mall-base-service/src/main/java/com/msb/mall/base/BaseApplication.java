package com.msb.mall.base;

import com.msb.framework.mq.annotation.MessageQueueScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@Slf4j
@MessageQueueScan(value = "com.msb.**.mq")
@SpringBootApplication
public class BaseApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(BaseApplication.class, args);
        System.out.println(1);
    }
}