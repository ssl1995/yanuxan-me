package com.msb.mall.marketing.api.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 活动商品表(ActivityProductSku)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Accessors(chain = true)
@Data
public class ActivityProductSkuDO implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 活动商品id
     */
    private Long activityProductId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品sku id
     */
    private Long productSkuId;

    /**
     * sku活动秒杀价格
     */
    private BigDecimal price;

    /**
     * sku原价（冗余）
     */
    private BigDecimal originalPrice;

    /**
     * 活动秒杀数量
     */
    private Integer number;

    /**
     * 活动库存
     */
    private Integer stock;
}
