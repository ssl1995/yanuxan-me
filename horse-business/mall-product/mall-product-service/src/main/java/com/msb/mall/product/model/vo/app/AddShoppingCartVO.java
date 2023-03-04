package com.msb.mall.product.model.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class AddShoppingCartVO {

    @ApiModelProperty("产品基本信息")
    private ProductSimpleVO productSimple;

    @ApiModelProperty("sku 基本信息")
    private ProductSkuSimpleVO productSkuSimple;

    @ApiModelProperty("增加购物车数量")
    private Integer shoppingCartNumber;

    @ApiModelProperty("实际最大可设置购物车数量")
    private Integer canSetShoppingCartNumber;

    @ApiModelProperty("是否超出最大限制")
    private Boolean isBeyondMaxLimit;
}
