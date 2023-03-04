package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("后管退款单-所属订单VO")
public class RefundTradeOrderInfoVO {

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

}
