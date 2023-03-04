package com.msb.mall.marketing.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ActivityProductSkuDTO implements Serializable {

    /**
     * 活动商品skuId（注意不是 productSkuId）
     */
    private Long activityProductSkuId;

    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品sku名称
     */
    private String skuName;
}
