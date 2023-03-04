package com.msb.mall.base.api;

import com.msb.mall.base.api.model.OrderConfigDO;

/**
 * @author shumengjiao
 */
public interface OrderConfigDubboService {
    /**
     * 查询订单配置
     * @return 订单配置
     */
    OrderConfigDO getOrderConfig();
}
