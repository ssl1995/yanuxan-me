package com.msb.im.module.waiter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 客服功能配置
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Component
@Data
@ConfigurationProperties(prefix = "store")
public class StoreProperties {
    private String avatar;
    private String name;
    private String id;
    private Integer sysId;
    /**
     * 商铺客服会话用户端发送心跳过期时间
     */
    private long storeUserHeartExpireSeconds = 180 * 60;
    /**
     * 商铺客服会话客服端未发送心跳过期时间
     */
    private long storeWaiterHeartExpireSeconds = 60;
}
