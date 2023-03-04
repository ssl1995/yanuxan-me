package com.msb.mall.product.model.dto.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品sku(ProductSku)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Data
public class ProductSkuDTO implements Serializable {

    @ApiModelProperty("skuId")
    private Long id;

    @ApiModelProperty("属性标记值组合，用逗号分隔（如:“1,3”，一个组合对应一个规格）")
    private String attributeSymbolList;

    @ApiModelProperty("属性名称，使用空格分隔（如：“黑色 大号”）")
    private String name;

    @Min(0)
    @ApiModelProperty("成本价")
    private BigDecimal costPrice;

    @Min(0)
    @NotNull
    @ApiModelProperty("售价")
    private BigDecimal sellPrice;

    @Min(0)
    @NotNull
    @ApiModelProperty("库存数量，新增库存时使用")
    private Integer stock;

    @ApiModelProperty("库存变更值，修改库存数量时使用（可以为负数）")
    private Integer stockChange;

    @NotNull
    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("预警库存商品数量")
    private Integer stockWarn;

}

