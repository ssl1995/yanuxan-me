package com.msb.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * IM线程池属性配置
 *
 * @author zhou miao
 * @date 2022/04/19
 */
@ConfigurationProperties(prefix = "im.thread-pool")
@Data
public class ThreadPoolProperties {
    private int coreSize;
    private int maxSize;
    private int keepAliveTime;
    private int queueSize;
    private String namePrefix;
}