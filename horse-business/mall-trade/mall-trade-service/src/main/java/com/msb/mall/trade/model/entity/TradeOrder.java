package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import com.msb.framework.mysql.shard.TableHashShardStrategy;
import com.msb.framework.mysql.shard.TableShard;
import com.msb.framework.mysql.shard.TableShardField;
import com.msb.framework.mysql.shard.TableShardStrategy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 交易订单(TradeOrder)表实体类
 *
 * @author makejava
 * @since 2022-03-26 16:29:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("trade_order")
@TableShard(shardStrategy = TableHashShardStrategy.class, hashShard = 3)
public class TradeOrder extends BaseEntity implements Serializable {

    /**
     * 订单ID
     */
    @TableShardField
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 运费
     */
    private BigDecimal shippingAmount;
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    /**
     * 实付金额
     */
    private BigDecimal payAmount;
    /**
     * 已退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 下单时间
     */
    private LocalDateTime submitTime;
    /**
     * 失效时间
     */
    private LocalDateTime expireTime;
    /**
     * 自动收货时间
     */
    private LocalDateTime autoReceiveTime;
    /**
     * 售后截止时间
     */
    private LocalDateTime afterSaleDeadlineTime;
    /**
     * 用户留言
     */
    private String userMessage;
    /**
     * 取消原因
     */
    private String cancelReason;
    /**
     * 订单来源（1：未知来源，2：安卓端APP，3：IOS端APP，4：H5浏览器，5：微信浏览器，6：PC浏览器）
     */
    private Integer orderSource;
    /**
     * 订单类型（1：普通订单，2：免费订单，3：秒杀订单）
     */
    private Integer orderType;
    /**
     * 订单状态（1：待支付，2：已关闭，3：已支付，4：已发货，5：已收货，6：已完成）
     */
    private Integer orderStatus;
    /**
     * 支付方式（1：未支付，2：微信支付，3：支付宝支付）
     */
    private Integer payType;
    /**
     * 是否包邮（0：包邮，1：不包邮）
     */
    private Boolean isPackageFree;
    /**
     * 是否开启售后（0：未开启售后，1：已开启售后）
     */
    private Boolean isAfterSale;
    /**
     * 是否禁用（0：启用，1：禁用）
     */
    private Boolean isDisabled;
    /**
     * 是否删除（0：未删除，1：已删除）
     */
    private Boolean isDeleted;
    /**
     * 自动好评时间
     */
    private LocalDateTime autoPraise;
    /**
     * 收货时间
     */
    private LocalDateTime receiveTime;

}

