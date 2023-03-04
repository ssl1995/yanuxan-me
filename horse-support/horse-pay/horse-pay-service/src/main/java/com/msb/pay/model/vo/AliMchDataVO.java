package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("支付宝商户资料信息VO")
public class AliMchDataVO {

    @ApiModelProperty(value = "支付宝公钥")
    private String publicKey;

    @ApiModelProperty(value = "是否上传 支付宝公钥证书")
    private Boolean hasAliPayCert;

    @ApiModelProperty(value = "是否上传 支付宝根证书")
    private Boolean hasAliPayRootCert;

}
