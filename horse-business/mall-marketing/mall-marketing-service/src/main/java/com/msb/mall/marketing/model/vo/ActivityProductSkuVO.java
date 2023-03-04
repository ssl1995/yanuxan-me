package com.msb.mall.marketing.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动商品表(ActivityProductSku)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Data
@Accessors(chain = true)
public class ActivityProductSkuVO implements Serializable {

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

    @ApiModelProperty("活动已售数量")
    private Integer activitySellNumber;

    @ApiModelProperty("库存")
    private Integer stock;

}

