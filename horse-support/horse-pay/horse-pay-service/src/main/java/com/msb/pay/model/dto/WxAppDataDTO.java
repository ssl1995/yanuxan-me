package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微信应用资料信息VO")
public class WxAppDataDTO {

    @ApiModelProperty(value = "应用秘钥")
    private String appSecret;

}
