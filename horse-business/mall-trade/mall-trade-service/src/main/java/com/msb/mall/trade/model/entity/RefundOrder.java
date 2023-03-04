package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.*;
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
 * @date 2022-04-08 18:24:33
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("refund_order")
public class RefundOrder extends BaseEntity implements Serializable {

    /**
     * 退款单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单详情ID
     */
    private Long orderProductId;

    /**
     * 商品金额
     */
    private BigDecimal productAmount;

    /**
     * 退运费金额
     */
    private BigDecimal backShippingAmount;

    /**
     * 申请退款金额
     */
    private BigDecimal applyAmount;

    /**
     * 实际退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 问题描述
     */
    private String problemDescribe;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 关闭原因
     */
    private String closeReason;

    /**
     * 退款申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 商家处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 商家处理到期时间
     */
    private LocalDateTime handleExpireTime;

    /**
     * 用户退货到期时间
     */
    private LocalDateTime returnExpireTime;

    /**
     * 商家收货到期时间
     */
    private LocalDateTime receivingExpireTime;

    /**
     * 收货状态（1：未收到货，2：已收到货）
     */
    private Integer receiveStatus;

    /**
     * 退款单类型（1：仅退款，2：退货退款）
     */
    private Integer refundType;

    /**
     * 退款单状态（1：已申请，2：已关闭，3：待退货，4：退货中，5：退款中，6：退款成功，7：退款失败）
     */
    private Integer refundStatus;

    /**
     * 商家处理状态（1：待处理，2：同意退款，3：拒绝退款，4：同意退货，5：拒绝退货，6：确认收货，7：拒绝收货）
     */
    private Integer handleStatus;

    /**
     * 是否退运费（0：否，1：是）
     */
    private Boolean isBackShippingAmount;

    /**
     * 删除状态（0：未删除，1：已删除）
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
