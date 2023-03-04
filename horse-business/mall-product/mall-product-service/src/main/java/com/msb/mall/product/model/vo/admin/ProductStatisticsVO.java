package com.msb.mall.product.model.vo.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品统计VO
 * @author shumengjiao
 * @date 2022-05-30
 */
@Data
public class ProductStatisticsVO implements Serializable {

    @ApiModelProperty("全部商品数量")
    private Integer allCount;

    @ApiModelProperty("已下架商品数量")
    private Integer takeDownCount;

    @ApiModelProperty("已上架商品数量")
    private Integer onTheShelfCount;

    @ApiModelProperty("库存紧张商品数量")
    private Integer stockLessCount;
}
