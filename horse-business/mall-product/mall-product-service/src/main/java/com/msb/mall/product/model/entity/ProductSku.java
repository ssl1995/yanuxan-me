package com.msb.mall.product.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.msb.framework.mysql.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品sku(ProductSku)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("product_sku")
public class ProductSku extends BaseEntity implements Serializable {

    /**
     * SKU ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 属性值列表，关联属性表中的symbol，用逗号分隔，如:“1,3,4”
     */
    private String attributeSymbolList;

    /**
     * sku名称，如商品鼠标的规格名为“黑色 有线”
     */
    private String name;

    /**
     * 售价
     */
    private BigDecimal sellPrice;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 启用状态:1-启用,0-未启用
     */
    private Boolean isEnable;

    /**
     * 预警库存商品数量
     */
    private Integer stockWarn;


}
