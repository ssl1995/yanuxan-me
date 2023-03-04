package com.msb.pay.model.paydata;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("支付宝扫码支付数据")
public class AliQrData extends PayData implements Serializable {

    @ApiModelProperty("付款二维码数据")
    private String qrCodeData;

}
