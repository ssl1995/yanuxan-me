package com.msb.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.msb.business")
public class DemoConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoConsumerApplication.class, args);
    }
}