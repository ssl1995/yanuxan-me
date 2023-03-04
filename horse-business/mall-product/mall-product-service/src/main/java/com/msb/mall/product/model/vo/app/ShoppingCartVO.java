package com.msb.mall.product.model.vo.app;


import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.product.enums.ProductTypeEnums;
import com.msb.mall.product.model.vo.app.ProductSimpleVO;
import com.msb.mall.product.model.vo.app.ProductSkuSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * (ShoppingCart)表实体类
 *
 * @author makejava
 * @date 2022-03-31 16:16:10
 */
@Data
public class ShoppingCartVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品名称（备份）")
    private String productName;

    @ApiModelProperty("商品主图（备份）")
    private String productMainPicture;

    @ApiModelProperty("商品信息")
    private ProductSimpleVO product;

    @ApiModelProperty("商品sku id")
    private Long productSkuId;

    @ApiModelProperty("商品sku信息")
    private ProductSkuSimpleVO productSku;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("商品类型")
    @ApiModelPropertyEnum(dictEnum = ProductTypeEnums.class)
    private Integer productType;

}

