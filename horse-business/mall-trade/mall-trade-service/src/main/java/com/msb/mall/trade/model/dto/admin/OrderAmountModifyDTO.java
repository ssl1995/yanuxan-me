package com.msb.mall.trade.model.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("后管修改费用DTO")
public class OrderAmountModifyDTO {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "运费", required = true)
    private BigDecimal shippingAmount;

}
