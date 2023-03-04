package com.msb.mall.product.model.vo.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品sku(ProductSku)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Data
public class ProductSkuVO implements Serializable {

    @ApiModelProperty("SKU ID")
    private Long id;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("属性值列表，关联属性表中的symbol，用逗号分隔，如:“1,3,4”")
    private String attributeSymbolList;

    @ApiModelProperty("sku名称，如商品鼠标的规格名为“黑色 有线”")
    private String name;

    @ApiModelProperty("售价")
    private BigDecimal sellPrice;

    @ApiModelProperty("成本价")
    private BigDecimal costPrice;

    @ApiModelProperty("库存数量")
    private Integer stock;

    @ApiModelProperty("启用状态:1-启用,0-未启用")
    private Boolean isEnable;



}

