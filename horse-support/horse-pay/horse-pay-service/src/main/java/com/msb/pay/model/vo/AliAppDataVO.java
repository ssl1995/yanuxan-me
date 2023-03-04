package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("支付宝应用资料信息VO")
public class AliAppDataVO {

    @ApiModelProperty(value = "应用私钥")
    private String privateKey;

    @ApiModelProperty(value = "是否上传 应用证书")
    private Boolean hasAppCert;

}
