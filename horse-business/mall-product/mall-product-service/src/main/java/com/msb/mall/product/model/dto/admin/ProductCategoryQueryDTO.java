package com.msb.mall.product.model.dto.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品分类(ProductCategory)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 18:21:57
 */
@Data
public class ProductCategoryQueryDTO implements Serializable {

    @ApiModelProperty("父分类id")
    private Long parentId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("级别")
    private Integer level;

    @ApiModelProperty("是否显示")
    private Boolean isEnable;


}

