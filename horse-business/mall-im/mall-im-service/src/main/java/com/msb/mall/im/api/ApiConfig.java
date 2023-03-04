package com.msb.mall.im.api;

import com.msb.im.api.MessageApi;
import com.msb.im.api.StoreApi;
import com.msb.im.api.StoreWaiterApi;
import com.msb.im.api.UserApi;
import com.msb.mall.im.conifg.MallImConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ApiConfig {
    @Resource
    private MallImConfig mallImConfig;

    @Bean
    public MessageApi messageApi() {
        return new MessageApi(mallImConfig.getClient());
    }

    @Bean
    public StoreApi storeApi() {
        return new StoreApi(mallImConfig.getClient());
    }

    @Bean
    public StoreWaiterApi storeWaiterApi() {
        return new StoreWaiterApi(mallImConfig.getClient());
    }

    @Bean
    public UserApi userApi() {
        return new UserApi(mallImConfig.getClient());
    }
}
