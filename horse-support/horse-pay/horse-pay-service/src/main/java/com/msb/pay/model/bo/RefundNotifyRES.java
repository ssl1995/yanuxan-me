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
@ApiModel("退款回调处理响应")
public class RefundNotifyRES {

    @ApiModelProperty("支付渠道退款单号")
    private String channelRefundOrderNo;

    @ApiModelProperty("支付渠道通知参数")
    private String channelNotify;

    @ApiModelProperty("退款时间")
    private LocalDateTime successTime;

    @ApiModelProperty("退款是否成功")
    private Boolean refundSuccess;

}
