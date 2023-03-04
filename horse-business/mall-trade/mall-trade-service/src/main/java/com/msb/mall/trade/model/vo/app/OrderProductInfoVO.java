package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderPayTypeEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("APP商品订单详情VO")
public class OrderProductInfoVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelPropertyEnum(dictEnum = OrderStatusEnum.class)
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    @TransformEnum(value = OrderStatusEnum.class, from = "orderStatus")
    @ApiModelProperty("订单状态文本")
    private String orderStatusDesc;

    @ApiModelProperty("服务器时间")
    private LocalDateTime serverTime;

    @ApiModelProperty("下单时间")
    private LocalDateTime submitTime;

    @ApiModelProperty("支付失效时间")
    private LocalDateTime expireTime;

    @ApiModelProperty("自动收货时间")
    private LocalDateTime autoReceiveTime;

    @ApiModelProperty("售后截止时间")
    private LocalDateTime afterSaleDeadlineTime;

    @ApiModelPropertyEnum(dictEnum = OrderPayTypeEnum.class)
    @ApiModelProperty("支付方式")
    private Integer payType;

    @TransformEnum(value = OrderPayTypeEnum.class, from = "payType")
    @ApiModelProperty("支付方式文本")
    private String payTypeDesc;

    @ApiModelProperty("用户留言")
    private String userMessage;

    @ApiModelProperty("取消原因")
    private String cancelReason;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("运费")
    private BigDecimal shippingAmount;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;

    @Transform
    @ApiModelProperty("商品详情信息")
    private OrderProductVO orderProduct;

}
