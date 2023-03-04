package com.msb.mall.trade.third.logistics;

import com.msb.mall.trade.third.logistics.config.LogisticsApiConfig.ApiConfig;

/**
 * 快递API服务抽象类
 *
 * @author peng.xy
 * @date 2022/4/2
 */
public abstract class AbstractLogisticsApiClient implements LogisticsApiClient {

    protected ApiConfig apiConfig;

    /**
     * 初始化快递API
     *
     * @param apiConfig：apiConfig
     * @author peng.xy
     * @date 2022/4/2
     */
    @Override
    public void initApiConfig(ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    /**
     * 获取物流API配置ID
     *
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/4/6
     */
    @Override
    public String getLogisticsApiId() {
        return this.apiConfig.getId();
    }

}
