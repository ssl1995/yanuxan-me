package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author 86151
 */
@Data
@Accessors(chain = true)
public class FrontPageVisitorTotalVO {
    @ApiModelProperty("本周访客数量")
    private Integer weekVisitor;

    @ApiModelProperty("本周同比上周涨跌比例")
    private BigDecimal weekUpsOrDownsScale;

    @ApiModelProperty("本月访客数量")
    private Integer monthVisitor;

    @ApiModelProperty("本月同比上月涨跌比例")
    private BigDecimal monthUpsOrDownsScale;
}
