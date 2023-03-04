package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("预支付下单DTO")
public class PrepaymentDTO implements Serializable {

    @NotBlank
    @ApiModelProperty(value = "支付订单号", required = true)
    private String payOrderNo;

    @NotNull
    @DecimalMin("0.01")
    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;

    @NotBlank
    @ApiModelProperty(value = "商品标题", required = true)
    private String subject;

    @NotBlank
    @ApiModelProperty(value = "商品描述", required = true)
    private String body;

    @ApiModelProperty(value = "回调通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "回调页面地址")
    private String returnUrl;

}
