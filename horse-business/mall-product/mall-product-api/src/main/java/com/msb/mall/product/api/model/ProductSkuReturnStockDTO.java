package com.msb.mall.product.api.model;

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
public class ProductSkuReturnStockDTO implements Serializable {
    /**
     * sku id
     */
    private Long skuId;
    /**
     * 购买数量
     */
    private Integer quantity;
}
