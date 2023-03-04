package com.msb.pay.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("支付宝商户资料信息DTO")
public class AliMchDataDTO {

    @ApiModelProperty(value = "支付宝公钥")
    private String publicKey;

    @ApiModelProperty(value = "支付宝公钥证书 OSS下载路径")
    private String aliPayCertUrl;

    @ApiModelProperty(value = "支付宝根证书 OSS下载路径")
    private String aliPayRootCertUrl;

}
