package com.msb.pay.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.pay.enums.NotifyStatusEnum;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.enums.PayStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("支付订单分页列表VO")
public class PayOrderPageVO {

    @ApiModelProperty("支付订单ID")
    private Long payOrderId;

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("简要商户信息（可能为空）")
    private MchSimpleInfoVO mchInfo;

    @ApiModelProperty("应用主键ID")
    private Long appPrimaryId;

    @ApiModelProperty("简要应用信息（可能为空）")
    private AppSimpleInfoVO appInfo;

    @ApiModelProperty("支付方式")
    private String payCode;

    @TransformEnum(value = PayCodeEnum.class, from = "payCode")
    @ApiModelProperty("支付方式文本")
    private String payCodeText;

    @ApiModelProperty("支付订单号")
    private String payOrderNo;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("已退款金额")
    private BigDecimal refundAmount;

    @ApiModelPropertyEnum(dictEnum = PayStatusEnum.class)
    @ApiModelProperty("支付状态")
    private Integer payStatus;

    @TransformEnum(value = PayStatusEnum.class, from = "payStatus")
    @ApiModelProperty("支付状态文本")
    private String payStatusText;

    @ApiModelPropertyEnum(dictEnum = NotifyStatusEnum.class)
    @ApiModelProperty("通知状态")
    private Integer notifyStatus;

    @TransformEnum(value = NotifyStatusEnum.class, from = "notifyStatus")
    @ApiModelProperty("通知状态文本")
    private String notifyStatusText;

    @ApiModelProperty("下单时间")
    private LocalDateTime createTime;

}
