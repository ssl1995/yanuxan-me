package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 86151
 */
@Data
public class FrontPageProductVO {
    @ApiModelProperty("已上架数量")
    private Integer onTheShelfCount;

    @ApiModelProperty("已下架数量")
    private Integer takeDownCount;

    @ApiModelProperty("库存紧张数量")
    private Integer stockLessCount;

    @ApiModelProperty("全部商品数量")
    private Integer allCount;
}
