package com.msb.mall.trade.model.dto.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.OrderCancelReasonTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("APP取消订单DTO")
public class OrderCancelDTO {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotNull
    @Min(1)
    @Max(5)
    @ApiModelPropertyEnum(dictEnum = OrderCancelReasonTypeEnum.class)
    @ApiModelProperty(value = "订单取消原因类型", required = true)
    private Integer cancelReasonType;

}
