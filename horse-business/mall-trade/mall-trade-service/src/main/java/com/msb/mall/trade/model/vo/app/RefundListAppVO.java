package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.RefundStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("APP退款单列表VO")
public class RefundListAppVO {

    @ApiModelProperty("退款单ID")
    private Long refundId;

    @ApiModelProperty("退款单号")
    private String refundNo;

    @ApiModelProperty("订单详情ID")
    private Long orderProductId;

    @ApiModelProperty("申请退款金额")
    private BigDecimal applyAmount;

    @ApiModelProperty("实际退款金额")
    private BigDecimal refundAmount;

    @ApiModelPropertyEnum(dictEnum = RefundStatusEnum.class)
    @ApiModelProperty("退款单状态")
    private Integer refundStatus;

    @ApiModelProperty("退款单状态文本")
    @TransformEnum(value = RefundStatusEnum.class, from = "refundStatus")
    private String refundStatusDesc;

    @ApiModelProperty("退款单商品信息")
    private RefundProductVO refundProduct;

}
