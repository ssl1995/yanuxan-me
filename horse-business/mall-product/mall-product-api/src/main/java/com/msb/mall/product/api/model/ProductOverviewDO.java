package com.msb.mall.product.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 商品总览DO
 * @author shumengjiao
 */
@Accessors(chain = true)
@Data
public class ProductOverviewDO implements Serializable {
    @ApiModelProperty("已上架数量")
    private Integer onTheShelfCount;

    @ApiModelProperty("已下架数量")
    private Integer takeDownCount;

    @ApiModelProperty("库存紧张数量")
    private Integer stockLessCount;

    @ApiModelProperty("全部商品数量")
    private Integer allCount;
}
