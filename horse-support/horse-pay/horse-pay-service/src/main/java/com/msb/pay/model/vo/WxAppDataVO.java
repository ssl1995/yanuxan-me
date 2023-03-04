package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("微信应用资料信息VO")
public class WxAppDataVO {

    @ApiModelProperty(value = "应用秘钥")
    private String appSecret;

}
