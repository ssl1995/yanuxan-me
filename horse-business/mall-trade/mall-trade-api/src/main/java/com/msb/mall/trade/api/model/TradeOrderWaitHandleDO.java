package com.msb.mall.trade.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 交易订单待处理事务DO
 * @author shumengjiao
 */
@Accessors(chain = true)
@Data
public class TradeOrderWaitHandleDO implements Serializable {

    @ApiModelProperty("待付款订单数量")
    private Integer waitPayCount;

    @ApiModelProperty("待发货订单数量")
    private Integer waitShipCount;

    @ApiModelProperty("已发货订单数量")
    private Integer shippedCount;

    @ApiModelProperty("待处理退款订单数量")
    private Integer waitRefundCount;

    @ApiModelProperty("待确认退货订单数量")
    private Integer waitReturnGoodsCount;

}
