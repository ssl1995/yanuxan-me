package com.msb.pay.mq.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("支付订单延时通知消息")
public class PayOrderDelayNotifyMessage implements Serializable {

    @ApiModelProperty("支付中台延迟级别")
    private Integer payCenterDelayLevel = 0;

    @ApiModelProperty("支付订单ID")
    private Long payOrderId;

}
