package com.msb.mall.base.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 订单配置表(OrderConfig)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-11 10:54:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order_config")
public class OrderConfig extends BaseEntity implements Serializable {

    /**
     * 订单配置id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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

