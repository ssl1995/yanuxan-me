package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("APP待收货订单物流信息列表VO")
public class OrderLogisticsListVO {

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

    @ApiModelPropertyEnum(dictEnum = OrderTypeEnum.class)
    @ApiModelProperty("订单类型")
    private Integer orderType;

    @TransformEnum(value = OrderTypeEnum.class, from = "orderType")
    @ApiModelProperty("订单类型文本")
    private String orderTypeDesc;

    @Transform
    @ApiModelProperty("商品信息列表")
    private List<OrderProductVO> products;

    @Transform
    @ApiModelProperty("物流信息")
    private OrderLogisticsVO logistics;

}
