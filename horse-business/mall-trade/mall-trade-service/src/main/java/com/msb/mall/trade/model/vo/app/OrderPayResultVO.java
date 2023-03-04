package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.BooleanEnum;
import com.msb.mall.trade.enums.OrderPayTypeEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("APP订单支付结果VO")
public class OrderPayResultVO {

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

    @ApiModelPropertyEnum(dictEnum = OrderPayTypeEnum.class)
    @ApiModelProperty("支付方式")
    private Integer payType;

    @TransformEnum(value = OrderPayTypeEnum.class, from = "payType")
    @ApiModelProperty("支付方式文本")
    private String payTypeDesc;

    @ApiModelPropertyEnum(dictEnum = BooleanEnum.class)
    @ApiModelProperty("支付是否成功")
    private Boolean isSuccess;

}
