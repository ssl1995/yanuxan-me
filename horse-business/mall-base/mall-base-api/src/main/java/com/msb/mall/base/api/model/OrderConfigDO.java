package com.msb.mall.base.api.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shumengjiao
 */
@Data
public class OrderConfigDO implements Serializable {
    /**
     * 自动收货时长(天)
     */
    private Integer automaticReceipt;

    /**
     * 售后过期时长(天)
     */
    private Integer afterSalesExpire;

    /**
     * 订单支付失效时长(分钟)
     */
    private Integer orderPayExpire;

    /**
     * 退货到期时长(天)
     */
    private Integer returnGoodsExpire;

    /**
     * 商家收货到期时长
     */
    private Integer merchantReceiptExpire;

    /**
     * 售后申请过期时长(天)
     */
    private Integer afterSalesApplicationExpire;

    /**
     * 自动好评时长(天)
     */
    private Integer automaticPraise;
}
