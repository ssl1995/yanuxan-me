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
 * 退款单支付中台对接(RefundOrderPayCenter)表实体类
 *
 * @author makejava
 * @date 2022-04-19 14:10:50
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("refund_order_pay_center")
public class RefundOrderPayCenter extends BaseEntity implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退款单ID
     */
    private Long refundId;

    /**
     * 支付订单ID
     */
    private String payOrderId;

    /**
     * 支付订单号
     */
    private String payOrderNo;

    /**
     * 退款订单ID
     */
    private String refundOrderId;

    /**
     * 退款订单号
     */
    private String refundOrderNo;

    /**
     * 支付应用代号
     */
    private String appCode;

    /**
     * 退款是否成功
     */
    private Boolean isSuccess;

}
