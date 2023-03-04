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
 * 活动商品表(ActivityProduct)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("activity_product")
public class ActivityProduct extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动对应时间段id
     */
    private Long activityTimeId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String productMainPicture;

    /**
     * 商品起售价
     */
    private BigDecimal productStartingPrice;

    /**
     * 分类id
     */
    private Long categoryId;


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
