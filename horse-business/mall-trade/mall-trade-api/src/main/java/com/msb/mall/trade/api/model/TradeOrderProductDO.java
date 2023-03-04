package com.msb.mall.trade.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 86151
 */
@Data
public class TradeOrderProductDO implements Serializable {

    @ApiModelProperty("详情ID")
    private Long id;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品SKU-ID")
    private Long productSkuId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品图片")
    private String productImageUrl;

    @ApiModelProperty("SKU规格")
    private String skuDescribe;

    @ApiModelProperty("购买数量")
    private Integer quantity;

    @ApiModelProperty("商品单价")
    private BigDecimal productPrice;

    @ApiModelProperty("实际价格")
    private BigDecimal realPrice;

    @ApiModelProperty("实际金额")
    private BigDecimal realAmount;

    @ApiModelProperty("活动ID")
    private Long activityId;

    @ApiModelProperty("明细状态（1：正常状态，2：申请售后，3：退款成功，4：退款失败）")
    private Integer detailStatus;

    @ApiModelProperty("活动类型（1：正常购买，2：免费领取，3：秒杀）")
    private Integer activityType;
}
