package com.msb.mall.trade.third.logistics;

import com.msb.framework.common.model.IDict;
import com.msb.mall.trade.third.logistics.impl.Express100ApiClient;
import com.msb.mall.trade.third.logistics.impl.VirtualLogisticsClient;

/**
 * 快递API服务枚举
 */
public enum LogisticsApiConfigEnum implements IDict<String> {

    // 快递API服务枚举
    VIRTUAL("virtual", "虚拟商品", VirtualLogisticsClient.class),
    EXPRESS100("express100", "快递100", Express100ApiClient.class),
    ;

    private final Class<? extends LogisticsApiClient> apiClient;

    LogisticsApiConfigEnum(String code, String text, Class<? extends LogisticsApiClient> apiClient) {
        init(code, text);
        this.apiClient = apiClient;
    }

    public Class<? extends LogisticsApiClient> getApiClient() {
        return apiClient;
    }

}
