package com.msb.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.msb")
public class PushServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PushServiceApplication.class, args);
    }
}