package com.msb.mall.product.model.dto.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
/**
 * 商品属性(ProductAttribute)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
public class ProductAttributeDTO implements Serializable {

    @NotNull
    @Size(max = 20)
    @ApiModelProperty("属性名")
    private String name;

    @ApiModelProperty("商品属性组ID，新增必传")
    private Long productAttributeGroupId;

    @ApiModelProperty("商品ID，新增必传")
    private Long productId;


}

