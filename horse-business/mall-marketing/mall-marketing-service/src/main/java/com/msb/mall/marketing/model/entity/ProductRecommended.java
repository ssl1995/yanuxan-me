package com.msb.mall.marketing.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 商品推荐表(ProductRecommended)表实体类
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_recommended")
public class ProductRecommended extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品主图
     */
    private String productMainPicture;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 是否开启 1开启，0未开启
     */
    private Boolean isEnable;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
