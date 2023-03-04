package com.msb.mall.trade.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 订单商品详情(TradeOrderProduct)表实体类
 *
 * @author makejava
 * @since 2022-03-24 18:30:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("trade_order_product")
public class TradeOrderProduct extends BaseEntity implements Serializable {

    /**
     * 详情ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品SKU-ID
     */
    private Long productSkuId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品图片
     */
    private String productImageUrl;
    /**
     * SKU规格
     */
    private String skuDescribe;
    /**
     * 购买数量
     */
    private Integer quantity;
    /**
     * 商品单价
     */
    private BigDecimal productPrice;
    /**
     * 实际价格
     */
    private BigDecimal realPrice;
    /**
     * 实际金额
     */
    private BigDecimal realAmount;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 明细状态（1：正常状态，2：申请售后，3：退款成功，4：退款失败）
     */
    private Integer detailStatus;
    /**
     * 活动类型（1：正常购买，2：免费领取，3：秒杀）
     */
    private Integer activityType;
    /**
     * 评价状态（1：待评价，2：已评价，3：已追评）
     */
    private Integer commentStatus;

}

