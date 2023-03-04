package com.msb.pay.mq.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("证书同步通知消息")
public class CertSyncNotifyMessage implements Serializable {

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("应用主键ID")
    private Long appPrimaryId;

}
