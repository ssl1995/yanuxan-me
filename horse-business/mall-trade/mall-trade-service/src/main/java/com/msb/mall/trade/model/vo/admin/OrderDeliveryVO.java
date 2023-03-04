package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import com.msb.mall.trade.model.vo.app.OrderLogisticsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("后管订单发货VO")
public class OrderDeliveryVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelPropertyEnum(dictEnum = OrderTypeEnum.class)
    @ApiModelProperty("订单类型")
    private Integer orderType;

    @TransformEnum(value = OrderTypeEnum.class, from = "orderType")
    @ApiModelProperty("订单类型文本")
    private String orderTypeDesc;

    @Transform
    @ApiModelProperty("物流信息")
    private OrderLogisticsVO logistics;

}
