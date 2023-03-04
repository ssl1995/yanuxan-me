package com.msb.pay.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel("支付回调处理响应")
public class PayNotifyRES {

    @ApiModelProperty("支付渠道订单号")
    private String channelPayOrderNo;

    @ApiModelProperty("支付渠道用户ID")
    private String channelUserId;

    @ApiModelProperty("支付渠道通知参数")
    private String channelNotify;

    @ApiModelProperty("支付是否成功")
    private Boolean paySuccess;

    @ApiModelProperty("支付时间")
    private LocalDateTime successTime;

}
