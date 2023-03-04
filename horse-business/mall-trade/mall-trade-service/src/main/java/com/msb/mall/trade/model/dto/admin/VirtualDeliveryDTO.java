package com.msb.mall.trade.model.dto.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("虚拟订单发货DTO")
public class VirtualDeliveryDTO {

    @NotEmpty
    @ApiModelProperty(value = "订单ID列表（1,2,3）", required = true)
    private List<Long> orderIds;

}
