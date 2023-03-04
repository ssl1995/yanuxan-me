package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.RefundReceiveStatusDesc;
import com.msb.mall.trade.enums.RefundStatusEnum;
import com.msb.mall.trade.enums.RefundTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel("APP退款单详情VO")
public class RefundInfoAppVO {

    @ApiModelProperty("退款单ID")
    private Long refundId;

    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单详情ID")
    private Long orderProductId;

    @ApiModelProperty("申请退款金额")
    private BigDecimal applyAmount;

    @ApiModelProperty("实际退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("退款原因")
    private String refundReason;

    @ApiModelProperty("退款原因类型")
    private Integer refundReasonType;

    @ApiModelProperty("问题描述")
    private String problemDescribe;

    @ApiModelProperty("关闭原因")
    private String closeReason;

    @ApiModelProperty("商家同意退货备注")
    private String agreeReturnRemark;

    @ApiModelProperty("用户退货物流备注")
    private String completeReturnRemark;

    @ApiModelProperty("服务器时间")
    private LocalDateTime serverTime;

    @ApiModelProperty("退款申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty("商家处理到期时间")
    private LocalDateTime handleExpireTime;

    @ApiModelProperty("用户退货到期时间")
    private LocalDateTime returnExpireTime;

    @ApiModelProperty("商家收货到期时间")
    private LocalDateTime receivingExpireTime;

    @ApiModelPropertyEnum(dictEnum = RefundTypeEnum.class)
    @ApiModelProperty("退款单类型")
    private Integer refundType;

    @TransformEnum(value = RefundTypeEnum.class, from = "refundType")
    @ApiModelProperty("退款单类型文本")
    private String refundTypeDesc;

    @ApiModelPropertyEnum(dictEnum = RefundStatusEnum.class)
    @ApiModelProperty("退款单状态")
    private Integer refundStatus;

    @ApiModelProperty("退款单状态文本")
    @TransformEnum(value = RefundStatusEnum.class, from = "refundStatus")
    private String refundStatusDesc;

    @ApiModelPropertyEnum(dictEnum = RefundReceiveStatusDesc.class)
    @ApiModelProperty("收货状态")
    private Integer receiveStatus;

    @ApiModelProperty("收货状态文本")
    @TransformEnum(value = RefundReceiveStatusDesc.class, from = "receiveStatus")
    private String receiveStatusDesc;

    @Transform
    @ApiModelProperty("退款单商品信息")
    private RefundProductVO refundProduct;

    @Transform
    @ApiModelProperty("退款单物流信息")
    private RefundLogisticsVO refundLogistics;

    @Transform
    @ApiModelProperty("退款单凭证信息")
    private List<RefundEvidenceVO> refundEvidences;

    public RefundInfoAppVO() {
        this.refundEvidences = Collections.emptyList();
    }

}
