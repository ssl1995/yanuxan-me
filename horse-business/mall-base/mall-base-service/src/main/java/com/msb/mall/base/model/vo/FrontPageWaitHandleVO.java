package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 待处理事务VO
 * @author 86151
 */
@Data
public class FrontPageWaitHandleVO {
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
