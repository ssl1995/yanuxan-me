package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("应用资料信息DTO")
public class BaseAppDataDTO {

    @ApiModelProperty(value = "微信应用信息")
    private WxAppDataDTO wxAppData;

    @ApiModelProperty(value = "支付宝应用信息")
    private AliAppDataDTO AliAppData;

}
