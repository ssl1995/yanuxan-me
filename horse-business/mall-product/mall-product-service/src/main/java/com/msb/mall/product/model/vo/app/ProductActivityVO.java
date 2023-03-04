package com.msb.mall.product.model.vo.app;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 商品详情页VO
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
@Accessors(chain = true)
public class ProductActivityVO implements Serializable {

    @ApiModelProperty("是否活动产品")
    private Boolean isActivity;

    @ApiModelProperty("是否开始活动")
    private Boolean isStartActivity;

    @ApiModelProperty("活动开始时间")
    private LocalDateTime activityStartTime;

    @ApiModelProperty("活动结束时间")
    private LocalDateTime activityEndTime;

    @ApiModelProperty("当前时间")
    private LocalDateTime currentTime;

    @ApiModelProperty("活动起售价")
    private BigDecimal activityPrice;

    @ApiModelProperty("活动id")
    private Long activityId;

    @ApiModelProperty("活动时间段id")
    private Long activityTimeId;
}

