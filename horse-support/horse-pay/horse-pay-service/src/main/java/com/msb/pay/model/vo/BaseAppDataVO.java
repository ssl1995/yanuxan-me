package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("应用资料信息VO")
public class BaseAppDataVO {

    @ApiModelProperty(value = "微信应用信息")
    private WxAppDataVO wxAppData;

    @ApiModelProperty(value = "支付宝应用信息")
    private AliAppDataVO aliAppData;

}
