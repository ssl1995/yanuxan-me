package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.BooleanEnum;
import com.msb.mall.trade.enums.RefundReceiveStatusDesc;
import com.msb.mall.trade.enums.RefundStatusEnum;
import com.msb.mall.trade.enums.RefundTypeEnum;
import com.msb.mall.trade.model.vo.app.RefundEvidenceVO;
import com.msb.mall.trade.model.vo.app.RefundProductVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel("后管退款单详情VO")
public class RefundInfoAdminVO {

    @ApiModelProperty("退款单ID")
    private Long refundId;

    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单详情ID")
    private Long orderProductId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户号码")
    private String userPhone;

    @ApiModelProperty("用户昵称")
    private String userNickName;

    @ApiModelProperty("运费金额")
    private BigDecimal backShippingAmount;

    @ApiModelProperty("订单商品金额")
    private BigDecimal productAmount;

    @ApiModelProperty("申请退款金额")
    private BigDecimal applyAmount;

    @ApiModelProperty("实际退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("退款申请时间")
    private LocalDateTime applyTime;

    @ApiModelProperty("退款原因")
    private String refundReason;

    @ApiModelProperty("问题描述")
    private String problemDescribe;

    @ApiModelProperty("关闭原因")
    private String closeReason;

    @ApiModelPropertyEnum(dictEnum = RefundReceiveStatusDesc.class)
    @ApiModelProperty("收货状态")
    private Integer receiveStatus;

    @ApiModelProperty("收货状态文本")
    @TransformEnum(value = RefundReceiveStatusDesc.class, from = "receiveStatus")
    private String receiveStatusDesc;

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

    @ApiModelPropertyEnum(dictEnum = BooleanEnum.class)
    @ApiModelProperty("是否退运费")
    private Boolean isBackShippingAmount;

    @Transform
    @ApiModelProperty("退款单所属订单信息")
    private RefundTradeOrderInfoVO refundTradeOrderInfo;

    @Transform
    @ApiModelProperty("退款单商品信息")
    private RefundProductVO refundProduct;

    @Transform
    @ApiModelProperty("退款申请凭证信息")
    private List<RefundEvidenceVO> refundEvidences;

    @Transform
    @ApiModelProperty("操作日志列表")
    private List<RefundLogInfoVO> refundLogs;

    public RefundInfoAdminVO() {
        this.refundEvidences = Collections.emptyList();
        this.refundLogs = Collections.emptyList();
    }

}
