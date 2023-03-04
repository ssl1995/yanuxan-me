package com.msb.mall.product.api.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * (ShoppingCart)表实体类
 *
 * @author makejava
 * @date 2022-03-31 16:16:10
 */
@Data
public class ShoppingCartDO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品sku id")
    private Long productSkuId;

    @ApiModelProperty("数量")
    private Integer number;

}

