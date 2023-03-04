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
public class FrontPageSalesTotalVO {
    @ApiModelProperty("本周销售总额")
    private BigDecimal weekSales;

    @ApiModelProperty("本周同比上周涨跌比例")
    private BigDecimal weekUpsOrDownsScale;

    @ApiModelProperty("本月销售总额")
    private BigDecimal monthSales;

    @ApiModelProperty("本月同比上月涨跌比例")
    private BigDecimal monthUpsOrDownsScale;
}
