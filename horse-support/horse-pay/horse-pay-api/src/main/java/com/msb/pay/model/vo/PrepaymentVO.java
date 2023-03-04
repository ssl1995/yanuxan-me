package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("预支付下单VO")
public class PrepaymentVO implements Serializable {

    @ApiModelProperty("预支付订单ID")
    private String payOrderId;

    @ApiModelProperty("预支付订单号")
    private String payOrderNo;

    @ApiModelProperty("手机收银台页面")
    private String wapCashierUrl;

}
