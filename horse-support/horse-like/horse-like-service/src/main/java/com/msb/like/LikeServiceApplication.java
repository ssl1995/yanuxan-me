package com.msb.like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author shumengjiao
 */
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.msb")
public class LikeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LikeServiceApplication.class, args);
    }

}