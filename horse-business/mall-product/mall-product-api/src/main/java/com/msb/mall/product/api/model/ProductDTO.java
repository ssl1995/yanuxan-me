package com.msb.mall.product.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ProductDTO implements Serializable {
    /**
     * 商品ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 上下架状态:1-上架,0-下架
     */
    private Boolean isEnable;
}
