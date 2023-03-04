package com.msb.mall.trade.model.dto.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("提交预订单商品信息DTO")
public class AdvanceOrderDTO extends OrderSubmitProductDTO {

    @ApiModelPropertyEnum(dictEnum = BooleanEnum.class)
    @ApiModelProperty(value = "是否为虚拟商品订单", required = false)
    private Boolean isVirtual;

    @ApiModelProperty(value = "收货地址ID（非虚拟商品订单必传）", required = false)
    private Long recipientAddressId;

    public AdvanceOrderDTO() {
        super();
        this.isVirtual = false;
    }
}
