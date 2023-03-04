package com.msb.mall.product.model.vo.app;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * app商品属性VO
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
@ApiModel("商品属性")
public class ProductAttributeSimpleVO implements Serializable {


    @ApiModelProperty("属性名")
    private String name;

    @ApiModelProperty("标记值，同一个商品下唯一，用于检索属性")
    private Integer symbol;


}

