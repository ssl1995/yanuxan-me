package com.msb.pay.model.vo;

import com.msb.pay.model.BaseSign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("申请退款VO")
public class ApplyRefundVO extends BaseSign implements Serializable {

    @ApiModelProperty("支付代号")
    private String mchCode;

    @ApiModelProperty("应用代号")
    private String appCode;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("退款订单ID")
    private String refundOrderId;

    @ApiModelProperty("退款订单号")
    private String refundOrderNo;

    @ApiModelProperty("付款金额")
    private BigDecimal payAmount;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("申请是否成功")
    private Boolean applySuccess;

}
