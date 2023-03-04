package com.msb.pay.channel.wxpay.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微信应用资料信息")
public class WxAppDataInfo {

    @ApiModelProperty(value = "应用秘钥")
    private String appSecret;

}
