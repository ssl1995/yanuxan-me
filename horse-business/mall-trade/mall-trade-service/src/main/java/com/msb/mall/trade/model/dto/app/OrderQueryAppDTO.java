package com.msb.mall.trade.model.dto.app;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.OrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel("APP订单列表DTO")
public class OrderQueryAppDTO extends PageDTO {

    public OrderQueryAppDTO() {
        this.orderStatus = Collections.emptyList();
    }

    @ApiModelPropertyEnum(dictEnum = OrderStatusEnum.class)
    @ApiModelProperty(value = "订单状态，多值示例：1,2,3", required = false)
    private List<Integer> orderStatus;

}
