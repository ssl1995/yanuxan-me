package com.msb.mall.product.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (ShoppingCart)表实体类
 *
 * @author makejava
 * @date 2022-03-31 16:16:10
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shopping_cart")
public class ShoppingCart extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

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
     * 商品sku id
     */
    private Long productSkuId;

    /**
     * 数量
     */
    private Integer number;


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
