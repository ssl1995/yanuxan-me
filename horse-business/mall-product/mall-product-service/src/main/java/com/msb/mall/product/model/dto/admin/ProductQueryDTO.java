package com.msb.mall.product.model.dto.admin;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商品查询条件（适用于各种商品列表查询）
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("商品查询条件（适用于各种商品列表查询），默认包含上架条件")
public class ProductQueryDTO extends PageDTO {

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类ID")
    private Long categoryId;


}

