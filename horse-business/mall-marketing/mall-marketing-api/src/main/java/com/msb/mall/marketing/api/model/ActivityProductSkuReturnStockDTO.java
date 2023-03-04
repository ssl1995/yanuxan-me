package com.msb.mall.marketing.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商品sku返还库存DTO
 *
 * @author luozhan
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ActivityProductSkuReturnStockDTO implements Serializable {
    /**
     * activityProductSkuId
     */
    private Long activityProductSkuId;
    /**
     * 购买数量
     */
    private Integer quantity;
}
