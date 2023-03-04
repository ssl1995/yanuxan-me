package com.msb.pay.model.paydata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("支付宝PC数据")
public class AliPcData extends PayData implements Serializable {

    @ApiModelProperty("支付跳转链接")
    private String payUrl;

}
