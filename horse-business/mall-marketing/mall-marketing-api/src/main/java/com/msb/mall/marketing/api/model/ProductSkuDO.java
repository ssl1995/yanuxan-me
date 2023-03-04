package com.msb.mall.marketing.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductSkuDO implements Serializable {

    @ApiModelProperty("活动商品skuId")
    private Long id;

    @ApiModelProperty("活动商品id")
    private Long activityProductId;

    @ApiModelProperty("商品sku id")
    private Long productSkuId;

    @ApiModelProperty("sku名，如“黑色 大号”")
    private String skuName;

    @ApiModelProperty("售价")
    private BigDecimal originalPrice;

    @ApiModelProperty("sku活动秒杀价格")
    private BigDecimal price;

    @ApiModelProperty("活动秒杀数量")
    private Integer number;

    @ApiModelProperty("活动库存")
    private Integer stock;
}
