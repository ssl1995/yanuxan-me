package com.msb.mall.product.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商品sku下单DTO
 *
 * @author luozhan
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ProductSkuOccupyDTO implements Serializable {
    /**
     * sku id
     */
    private Long skuId;
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
