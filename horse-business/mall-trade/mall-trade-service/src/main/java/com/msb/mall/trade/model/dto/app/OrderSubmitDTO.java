package com.msb.mall.trade.model.dto.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.BooleanEnum;
import com.msb.mall.trade.enums.OrderSourceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("提交订单DTO")
public class OrderSubmitDTO {

    @ApiModelProperty(value = "收货地址ID（非虚拟商品订单必传）", required = false)
    private Long recipientAddressId;

    @NotNull
    @Min(1)
    @Max(6)
    @ApiModelPropertyEnum(dictEnum = OrderSourceEnum.class)
    @ApiModelProperty(value = "订单来源", required = true)
    private Integer orderSource;

    @Length(max = 64)
    @ApiModelProperty(value = "用户留言", required = false)
    private String userMessage;

    @ApiModelProperty(value = "购物车ID列表", required = false)
    private List<Long> shoppingCartIds;

    @ApiModelPropertyEnum(dictEnum = BooleanEnum.class)
    @ApiModelProperty(value = "是否为虚拟商品订单", required = false)
    private Boolean isVirtual;

    @Valid
    @NotEmpty
    @ApiModelProperty(value = "提交订单商品详情信息", required = true)
    private List<OrderSubmitProductDTO> products;

    public OrderSubmitDTO() {
        this.isVirtual = false;
    }
}
