package com.msb.pay.channel.alipay.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("支付宝应用资料信息")
public class AliAppDataInfo {

    @ApiModelProperty(value = "应用私钥")
    private String privateKey;

    @ApiModelProperty(value = "应用证书 OSS下载路径")
    private String appCertUrl;

    @ApiModelProperty(value = "应用证书 本地路径")
    private String appCertPath;

}
