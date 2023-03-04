package com.msb.user.auth;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Data
@ConfigurationProperties(prefix = "user.auth")
@Configuration
public class NoAuthConfig {
    private List<String> whiteList;
}
