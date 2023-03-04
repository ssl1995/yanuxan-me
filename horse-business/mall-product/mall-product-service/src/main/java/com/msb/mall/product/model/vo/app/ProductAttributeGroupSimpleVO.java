package com.msb.mall.product.model.vo.app;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * App商品属性
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
@ApiModel("商品属性组")
public class ProductAttributeGroupSimpleVO implements Serializable {

    @ApiModelProperty("商品属性组ID")
    private Long id;

    @ApiModelProperty("商品属性组名称")
    private String name;

    @ApiModelProperty("分组下属性列表")
    private List<ProductAttributeSimpleVO> attributes;

}

