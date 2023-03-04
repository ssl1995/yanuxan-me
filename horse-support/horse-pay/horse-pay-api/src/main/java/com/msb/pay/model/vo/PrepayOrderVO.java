package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("预支付订单VO")
public class PrepayOrderVO {

    @ApiModelProperty("支付订单ID")
    private Long payOrderId;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("商品标题")
    private String subject;

    @ApiModelProperty("商品描述")
    private String body;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("回调页面")
    private String returnUrl;

    @ApiModelProperty("微信支付应用（可能为空）")
    private PrepayAppVO prepayWxApp;

    @ApiModelProperty("支付宝应用（可能为空）")
    private PrepayAppVO prepayAliApp;

}
