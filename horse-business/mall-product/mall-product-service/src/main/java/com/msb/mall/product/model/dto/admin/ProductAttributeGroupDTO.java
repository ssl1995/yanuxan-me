package com.msb.mall.product.model.dto.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 商品属性组(ProductAttributeGroup)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
public class ProductAttributeGroupDTO implements Serializable {

    @NotNull
    @ApiModelProperty("商品ID")
    private Long productId;

    @NotNull
    @Size(max = 20)
    @ApiModelProperty("属性组名")
    private String name;


//    @ApiModelProperty("属性列表")
//    private List<ProductAttributeDTO> productAttributeList;
}

