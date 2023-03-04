package com.msb.mall.trade.model.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("APP撤销退款申请DTO")
public class RefundCancelDTO {

    @NotNull
    @ApiModelProperty(value = "退款单ID", required = true)
    private Long refundId;

}
