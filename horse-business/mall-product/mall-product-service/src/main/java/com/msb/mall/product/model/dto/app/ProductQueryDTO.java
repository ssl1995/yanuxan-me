package com.msb.mall.product.model.dto.app;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 后管商品查询条件
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("后管商品查询条件")
public class ProductQueryDTO extends PageDTO {

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "起售价", hidden = true)
    private BigDecimal startingPrice;


}

