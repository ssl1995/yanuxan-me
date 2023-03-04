package com.msb.mall.product.model.dto.app;


import lombok.Data;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;


/**
 * (ShoppingCart)表实体类
 *
 * @author makejava
 * @date 2022-03-31 16:16:10
 */
@Data
public class AddShoppingCartDTO implements Serializable {

    @ApiModelProperty("商品id")
    @NotNull
    private Long productId;

    @ApiModelProperty("商品sku id")
    @NotNull
    private Long productSkuId;

    @ApiModelProperty("设置的购买数量")
    @NotNull
    private Integer number;
}

