package com.msb.mall.marketing.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动商品表(ActivityProduct)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Data
@Accessors(chain = true)
public class ActivityProductVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("活动对应时间段id")
    private Long activityTimeId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品主图")
    private String productMainPicture;

    @ApiModelProperty("商品起售价")
    private BigDecimal productStartingPrice;
}

