package com.msb.mall.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = "com.msb")
public class TradeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeServiceApplication.class, args);
    }

}