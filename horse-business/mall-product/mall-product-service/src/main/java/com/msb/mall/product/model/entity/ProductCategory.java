package com.msb.mall.product.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 商品分类(ProductCategory)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_category")
public class ProductCategory extends BaseEntity implements Serializable {

    /**
     * 商品分类id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父分类id
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 图片地址
     */
    private String picture;

    /**
     * icon
     */
    private String icon;

    /**
     * 级别
     */
    private Integer level;
    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示
     */
    private Boolean isEnable;


}
