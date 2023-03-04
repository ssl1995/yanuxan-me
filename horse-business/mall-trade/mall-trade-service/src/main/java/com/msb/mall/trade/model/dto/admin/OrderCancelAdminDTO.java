package com.msb.mall.trade.model.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("后管取消订单DTO")
public class OrderCancelAdminDTO {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "取消原因", required = true)
    private String cancelReason;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty(value = "操作备注", required = true)
    private String remark;

}
