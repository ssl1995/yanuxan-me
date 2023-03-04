package com.msb.im.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * IM线程池对象配置
 *
 * @author zhou miao
 * @date 2022/04/19
 */
@Configuration
@EnableConfigurationProperties({ThreadPoolProperties.class})
public class ThreadPoolConfig {
    @Resource
    private ThreadPoolProperties threadPoolProperties;
    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(threadPoolProperties.getCoreSize(), r -> {
            Thread thread = new Thread(r);
            thread.setName(threadPoolProperties.getNamePrefix());
            return thread;
        }, new ThreadPoolExecutor.DiscardOldestPolicy());
    }
}
