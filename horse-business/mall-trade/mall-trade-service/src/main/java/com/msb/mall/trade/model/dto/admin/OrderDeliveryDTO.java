package com.msb.mall.trade.model.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("后管订单发货信息DTO")
public class OrderDeliveryDTO {

    @NotNull
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "物流公司编号", required = true)
    private String companyCode;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "物流公司名称", required = true)
    private String companyName;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty(value = "物流单号", required = true)
    private String trackingNo;

}
