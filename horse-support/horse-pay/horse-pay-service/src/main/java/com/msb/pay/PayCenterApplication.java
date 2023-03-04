package com.msb.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = "com.msb")
public class PayCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayCenterApplication.class, args);
    }

}