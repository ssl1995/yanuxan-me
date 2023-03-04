package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商户资料信息VO")
public class BaseMchDataVO {

    @ApiModelProperty("微信商户信息")
    private WxMchDataVO wxMchData;

    @ApiModelProperty("支付宝商户信息")
    private AliMchDataVO aliMchData;

}
