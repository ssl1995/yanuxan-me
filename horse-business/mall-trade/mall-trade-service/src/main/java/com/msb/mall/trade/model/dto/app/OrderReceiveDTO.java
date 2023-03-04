package com.msb.mall.trade.model.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("APP确认收货DTO")
public class OrderReceiveDTO {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

}
