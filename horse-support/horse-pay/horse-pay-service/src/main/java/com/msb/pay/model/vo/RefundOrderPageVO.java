package com.msb.pay.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.pay.enums.NotifyStatusEnum;
import com.msb.pay.enums.RefundStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel("退款订单分页列表VO")
public class RefundOrderPageVO {

    @ApiModelProperty("退款订单ID")
    private Long refundOrderId;

    @ApiModelProperty("商户主键ID")
    private Long mchPrimaryId;

    @ApiModelProperty("简要商户信息（可能为空）")
    private MchSimpleInfoVO mchInfo;

    @ApiModelProperty("应用主键ID")
    private Long appPrimaryId;

    @ApiModelProperty("简要应用信息（可能为空）")
    private AppSimpleInfoVO appInfo;

    @ApiModelProperty("支付订单ID")
    private Long payOrderId;

    @Transform
    @ApiModelProperty("简要支付订单信息（可能为空）")
    private PayOrderSimpleVO payOrderInfo;

    @ApiModelProperty("退款订单号")
    private String refundOrderNo;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelPropertyEnum(dictEnum = RefundStatusEnum.class)
    @ApiModelProperty("退款状态")
    private Integer refundStatus;

    @TransformEnum(value = RefundStatusEnum.class, from = "refundStatus")
    @ApiModelProperty("退款状态文本")
    private String refundStatusText;

    @ApiModelPropertyEnum(dictEnum = NotifyStatusEnum.class)
    @ApiModelProperty("通知状态")
    private Integer notifyStatus;

    @TransformEnum(value = NotifyStatusEnum.class, from = "notifyStatus")
    @ApiModelProperty("通知状态文本")
    private String notifyStatusText;

    @ApiModelProperty("申请时间")
    private LocalDateTime createTime;

}
