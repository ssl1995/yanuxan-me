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
 * 商品属性组(ProductAttributeGroup)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("product_attribute_group")
public class ProductAttributeGroup extends BaseEntity implements Serializable {

    /**
     * 商品属性组ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 属性组名
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;
}
