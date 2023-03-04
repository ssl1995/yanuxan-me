package com.msb.mall.marketing.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * 活动商品表(ActivityProductSku)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("activity_product_sku")
public class ActivityProductSku extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动商品id
     */
    private Long activityProductId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品sku id
     */
    private Long productSkuId;

    /**
     * sku活动秒杀价格
     */
    private BigDecimal price;

    /**
     * sku原价（冗余）
     */
    private BigDecimal originalPrice;

    /**
     * 活动秒杀数量
     */
    private Integer number;

    /**
     * 活动库存
     */
    private Integer stock;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
