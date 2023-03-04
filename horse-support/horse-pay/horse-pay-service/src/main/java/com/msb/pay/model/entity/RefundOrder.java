package com.msb.pay.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 退款订单表(RefundOrder)表实体类
 *
 * @author makejava
 * @date 2022-06-06 10:42:46
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("refund_order")
public class RefundOrder extends BaseEntity implements Serializable {

    /**
     * 退款订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商户主键ID
     */
    private Long mchPrimaryId;

    /**
     * 应用主键ID
     */
    private Long appPrimaryId;

    /**
     * 支付订单ID
     */
    private Long payOrderId;

    /**
     * 退款订单号
     */
    private String refundOrderNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 退款状态（1：退款中，2：退款关闭，3：退款成功，4：退款失败）
     */
    private Integer refundStatus;

    /**
     * 通知状态（1：未通知，2：已通知，3：已响应）
     */
    private Integer notifyStatus;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 渠道退款单号
     */
    private String channelRefundOrderNo;

    /**
     * 退款渠道发起参数
     */
    private String channelRequest;

    /**
     * 退款渠道响应参数
     */
    private String channelResponse;

    /**
     * 支付渠道回调参数
     */
    private String channelNotify;

    /**
     * 到期时间
     */
    private LocalDateTime expiredTime;

    /**
     * 退款时间
     */
    private LocalDateTime successTime;

}
