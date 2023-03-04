package com.msb.mall.marketing.model.dto;


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
public class ActivityProductSkuDTO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("活动商品id")
    private Long activityProductId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品sku id")
    private Long productSkuId;

    @ApiModelProperty("sku活动秒杀价格")
    private BigDecimal price;

    @ApiModelProperty("活动秒杀数量")
    private Long number;

    @ApiModelProperty("活动库存")
    private Integer stock;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新人id")
    private Long updateUser;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}

