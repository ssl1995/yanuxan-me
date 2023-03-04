package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderPayTypeEnum;
import com.msb.mall.trade.enums.OrderSourceEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import com.msb.mall.trade.model.vo.app.OrderLogisticsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("后管订单信息VO")
public class OrderInfoAdminVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户号码")
    private String userPhone;

    @ApiModelProperty("用户留言")
    private String userMessage;

    @ApiModelPropertyEnum(dictEnum = OrderStatusEnum.class)
    @ApiModelProperty("订单状态")
    private Integer orderStatus;

    @TransformEnum(value = OrderStatusEnum.class, from = "orderStatus")
    @ApiModelProperty("订单状态文本")
    private String orderStatusDesc;

    @ApiModelPropertyEnum(dictEnum = OrderPayTypeEnum.class)
    @ApiModelProperty("支付方式")
    private Integer payType;

    @TransformEnum(value = OrderPayTypeEnum.class, from = "payType")
    @ApiModelProperty("支付方式文本")
    private String payTypeDesc;

    @ApiModelPropertyEnum(dictEnum = OrderSourceEnum.class)
    @ApiModelProperty("订单来源")
    private Integer orderSource;

    @TransformEnum(value = OrderSourceEnum.class, from = "orderSource")
    @ApiModelProperty("订单来源文本")
    private String orderSourceDesc;

    @ApiModelPropertyEnum(dictEnum = OrderTypeEnum.class)
    @ApiModelProperty("订单类型")
    private Integer orderType;

    @TransformEnum(value = OrderTypeEnum.class, from = "orderType")
    @ApiModelProperty("订单类型文本")
    private String orderTypeDesc;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("运费")
    private BigDecimal shippingAmount;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("自动确认收货时间（天）")
    private Integer receiveExpire;

    @Transform
    @ApiModelProperty("物流信息")
    private OrderLogisticsVO logistics;

    @Transform
    @ApiModelProperty("商品信息")
    private List<OrderProductAdminVO> products;

    @Transform
    @ApiModelProperty("操作日志列表")
    private List<OrderLogVO> orderLogs;

}
