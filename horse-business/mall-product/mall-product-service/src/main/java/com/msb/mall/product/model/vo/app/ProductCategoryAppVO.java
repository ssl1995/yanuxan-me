package com.msb.mall.product.model.vo.app;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 商品分类及商品Vo
 *
 * @author luozhan
 * @date 2022-03-29 18:21:57
 */
@Data
public class ProductCategoryAppVO implements Serializable {

    @ApiModelProperty("商品分类id")
    private Long id;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("图片")
    private String picture;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("商品列表")
    private List<ProductSimpleVO> productList;

}

