package com.msb.third;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.msb")
public class ThirdServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdServiceApplication.class, args);
    }
}