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
 * 支付订单表(PayOrder)表实体类
 *
 * @author makejava
 * @date 2022-06-06 10:42:45
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("pay_order")
public class PayOrder extends BaseEntity implements Serializable {

    /**
     * 支付订单ID
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
     * 支付方式
     */
    private String payCode;

    /**
     * 支付订单号
     */
    private String payOrderNo;

    /**
     * 商品标题
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 已退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款次数
     */
    private Integer refundTimes;

    /**
     * 支付状态（1：支付中，2：已关闭，3：支付成功，4：支付失败，5：部分退款，6：全额退款）
     */
    private Integer payStatus;

    /**
     * 通知状态（1：未通知，2：已通知，3：已响应）
     */
    private Integer notifyStatus;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 回调页面
     */
    private String returnUrl;

    /**
     * 支付渠道订单号
     */
    private String channelPayOrderNo;

    /**
     * 支付渠道用户ID
     */
    private String channelUserId;

    /**
     * 支付渠道发起参数
     */
    private String channelRequest;

    /**
     * 支付渠道响应参数
     */
    private String channelResponse;

    /**
     * 支付渠道回调参数
     */
    private String channelNotify;

    /**
     * 失效时间
     */
    private LocalDateTime expiredTime;

    /**
     * 支付时间
     */
    private LocalDateTime successTime;

}
