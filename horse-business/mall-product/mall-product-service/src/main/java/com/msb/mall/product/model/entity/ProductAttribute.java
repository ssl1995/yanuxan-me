package com.msb.mall.product.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * 商品属性(ProductAttribute)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("product_attribute")
public class ProductAttribute extends BaseEntity implements Serializable {

    /**
     * 属性ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 属性名
     */
    private String name;

    /**
     * 商品属性组ID
     */
    private Long productAttributeGroupId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 标记值，同一个商品下唯一，用于检索属性
     */
    private Integer symbol;

    /**
     * 排序
     */
    private Integer sort;

}
