package com.msb.mall.trade.model.dto.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.ActivityTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@ApiModel("提交订单商品信息DTO")
public class OrderSubmitProductDTO {

    @NotNull
    @ApiModelProperty(value = "商品SKU-ID", required = true)
    private Long productSkuId;

    @Min(1)
    @ApiModelProperty(value = "购买数量", required = true)
    private Integer quantity;

    @Min(1)
    @Max(3)
    @NotNull
    @ApiModelPropertyEnum(dictEnum = ActivityTypeEnum.class)
    @ApiModelProperty(value = "活动类型", required = true)
    private Integer activityType;

    @ApiModelProperty(value = "活动ID（参加活动时必填）", required = false)
    private Long activityId;

    @ApiModelProperty(value = "活动时间ID（参加活动时必填）", required = false)
    private Long activityTimeId;

    public OrderSubmitProductDTO() {
        this.activityType = 1;
    }
}
