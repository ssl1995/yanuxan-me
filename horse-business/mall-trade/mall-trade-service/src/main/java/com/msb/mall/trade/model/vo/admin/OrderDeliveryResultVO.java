package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单发货结果VO")
public class OrderDeliveryResultVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelPropertyEnum(dictEnum = BooleanEnum.class)
    @ApiModelProperty("是否发货成功")
    private Boolean isSuccess;

    @ApiModelProperty("错误信息")
    private String message;

}
