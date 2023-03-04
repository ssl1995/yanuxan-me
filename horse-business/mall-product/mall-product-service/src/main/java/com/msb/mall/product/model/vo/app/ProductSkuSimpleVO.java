package com.msb.mall.product.model.vo.app;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品sku简要信息
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Accessors(chain = true)
@Data
public class ProductSkuSimpleVO implements Serializable {

    @ApiModelProperty("SKU ID")
    private Long skuId;

    @ApiModelProperty("属性值列表，关联属性表中的symbol，用逗号分隔，如:“1,3,4”")
    private String attributeSymbolList;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("售价")
    private BigDecimal sellPrice;

    @ApiModelProperty("库存数量")
    private Integer stock;


}

