package com.msb.pay.model.bo;

import com.msb.pay.enums.RefundStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel("退款申请响应")
public class ApplyRefundRES {

    @ApiModelProperty("支付代号")
    private String mchCode;

    @ApiModelProperty("应用代号")
    private String appCode;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("退款订单号")
    private String refundOrderNo;

    @ApiModelProperty("付款金额")
    private BigDecimal payAmount;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("退款渠道订单号")
    private String channelRefundOrderNo;

    @ApiModelProperty("退款渠道发起参数")
    private String channelRequest;

    @ApiModelProperty("退款渠道响应参数")
    private String channelResponse;

    @ApiModelProperty("申请是否成功")
    private Boolean applySuccess;

    @ApiModelProperty("退款状态")
    private RefundStatusEnum refundStatus;

}
