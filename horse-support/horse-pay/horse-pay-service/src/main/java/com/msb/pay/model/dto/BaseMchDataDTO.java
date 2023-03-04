package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商户资料信息DTO")
public class BaseMchDataDTO {

    @ApiModelProperty("微信商户信息")
    private WxMchDataDTO wxMchData;

    @ApiModelProperty("支付宝商户信息")
    private AliMchDataDTO aliMchData;

}
