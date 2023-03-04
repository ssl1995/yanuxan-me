package com.msb.mall.trade.model.dto.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("支付基础DTO")
public class BasePayDTO {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

}
