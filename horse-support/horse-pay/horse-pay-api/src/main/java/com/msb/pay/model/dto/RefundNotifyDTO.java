package com.msb.pay.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.RefundStatusEnum;
import com.msb.pay.model.BaseSign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("退款成功回调DTO")
public class RefundNotifyDTO extends BaseSign implements Serializable {

    @ApiModelProperty(value = "商户Id")
    private String mchId;

    @ApiModelProperty(value = "支付代号")
    private String mchCode;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "应用代号")
    private String appCode;

    @ApiModelProperty(value = "支付订单ID")
    private String payOrderId;

    @ApiModelProperty(value = "支付订单号")
    private String payOrderNo;

    @ApiModelProperty(value = "退款订单ID")
    private String refundOrderId;

    @ApiModelProperty(value = "退款订单号")
    private String refundOrderNo;

    @ApiModelProperty(value = "渠道退款单号")
    private String channelRefundOrderNo;

    @ApiModelPropertyEnum(dictEnum = RefundStatusEnum.class)
    @ApiModelProperty(value = "退款状态")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "退款时间")
    private String successTime;

}
