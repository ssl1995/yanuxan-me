package com.msb.mall.trade.model.dto.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("授权支付DTO")
public class AuthPayDTO extends BasePayDTO {

    @NotBlank
    @ApiModelProperty(value = "微信openId（JSAPI支付和小程序支付必传）", required = true)
    private String openId;

}
