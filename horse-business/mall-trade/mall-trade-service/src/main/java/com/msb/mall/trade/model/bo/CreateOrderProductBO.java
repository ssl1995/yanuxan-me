package com.msb.mall.trade.model.bo;

import com.msb.mall.base.api.model.ReceiveAddressDO;
import com.msb.mall.trade.enums.OrderTypeEnum;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单商品信息BO")
public class CreateOrderProductBO {

    @ApiModelProperty(value = "收货地址信息")
    ReceiveAddressDO receiveAddressDO;

    @ApiModelProperty(value = "偏远地区邮费")
    private BigDecimal remoteAreaPostage;

    @ApiModelProperty(value = "订单类型")
    private OrderTypeEnum orderType;

    @ApiModelProperty(value = "订单商品列表")
    private List<TradeOrderProduct> productList;

}
