package com.msb.mall.product.api.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品sku(ProductSku)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Accessors(chain = true)
@Data
public class ProductSkuDO implements Serializable {

    @ApiModelProperty("商品SKU-ID")
    private Long skuId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("商品名")
    private String productName;

    @ApiModelProperty("商品图片")
    private String productPicture;

    @ApiModelProperty("sku名，如“黑色 大号”")
    private String skuName;

    @ApiModelProperty("售价")
    private BigDecimal sellPrice;

    @ApiModelProperty("偏远地区邮费")
    private BigDecimal remoteAreaPostage;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("启用状态:1-启用,0-未启用")
    private Boolean isEnable;

    @ApiModelProperty("商品类型 1-实物商品 2-虚拟商品")
    private Integer productType;

}

