package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("微信商户资料信息VO")
public class WxMchDataVO {

    @ApiModelProperty(value = "API密钥")
    private String apiKey;

    @ApiModelProperty(value = "v3密钥")
    private String apiKeyV3;

    @ApiModelProperty(value = "证书序列号")
    private String serialNo;

    @ApiModelProperty(value = "是否上传 私钥证书")
    private Boolean hasKey;

    @ApiModelProperty(value = "是否上传 商户证书")
    private Boolean hasCert;

    @ApiModelProperty(value = "是否上传 p12证书")
    private Boolean hasCertP12;

    @ApiModelProperty(value = "是否上传 平台证书")
    private Boolean hasPlatformCert;

}
