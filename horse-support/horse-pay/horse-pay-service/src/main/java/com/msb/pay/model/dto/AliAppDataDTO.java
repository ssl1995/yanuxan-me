package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("支付宝应用资料信息DTO")
public class AliAppDataDTO {

    @ApiModelProperty(value = "应用私钥")
    private String privateKey;

    @ApiModelProperty(value = "应用证书 OSS下载路径")
    private String appCertUrl;

}
