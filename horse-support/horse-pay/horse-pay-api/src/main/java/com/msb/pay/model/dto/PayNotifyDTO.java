package com.msb.pay.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.pay.enums.PayStatusEnum;
import com.msb.pay.model.BaseSign;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("支付成功回调DTO")
public class PayNotifyDTO extends BaseSign implements Serializable {

    @ApiModelProperty(value = "商户Id")
    private String mchId;

    @ApiModelProperty(value = "支付代号")
    private String mchCode;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "应用代号")
    private String appCode;

    @ApiModelProperty(value = "支付方式")
    private String payCode;

    @ApiModelProperty(value = "支付订单ID")
    private String payOrderId;

    @ApiModelProperty(value = "支付订单号")
    private String payOrderNo;

    @ApiModelProperty(value = "支付渠道订单号")
    private String channelPayOrderNo;

    @ApiModelPropertyEnum(dictEnum = PayStatusEnum.class)
    @ApiModelProperty(value = "支付状态")
    private Integer payStatus;

    @ApiModelProperty(value = "商品标题")
    private String subject;

    @ApiModelProperty(value = "商品描述")
    private String body;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "支付时间")
    private String successTime;

}
