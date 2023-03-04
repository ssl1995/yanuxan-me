package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 交易订单支付中台对接(TradeOrderPayCenter)表实体类
 *
 * @author makejava
 * @date 2022-04-18 16:12:38
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("trade_order_pay_center")
public class TradeOrderPayCenter extends BaseEntity implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付订单ID
     */
    private String payOrderId;

    /**
     * 支付订单号
     */
    private String payOrderNo;

    /**
     * 支付应用代号
     */
    private String appCode;

    /**
     * 支付方式
     */
    private String payCode;

    /**
     * 支付是否成功
     */
    private Boolean isSuccess;

}
