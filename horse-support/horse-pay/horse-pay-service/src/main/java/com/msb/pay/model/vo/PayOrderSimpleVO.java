package com.msb.pay.model.vo;

import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.pay.enums.PayCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("支付订单简要信息VO")
public class PayOrderSimpleVO {

    @ApiModelProperty("支付订单ID")
    private Long payOrderId;

    @ApiModelProperty("支付方式")
    private String payCode;

    @TransformEnum(value = PayCodeEnum.class, from = "payCode")
    @ApiModelProperty("支付方式文本")
    private String payCodeText;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("订单金额")
    private BigDecimal amount;

}
