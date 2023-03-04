package com.msb.mall.marketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.msb")
public class MarketingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketingServiceApplication.class, args);
    }
}