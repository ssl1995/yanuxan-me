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
public class FrontPageOrderTotalVO {
    @ApiModelProperty("本周订单总数")
    private Integer weekOrder;

    @ApiModelProperty("本周同比上周涨跌比例")
    private BigDecimal weekUpsOrDownsScale;

    @ApiModelProperty("本月订单总数")
    private Integer monthOrder;

    @ApiModelProperty("本月同比上月涨跌比例")
    private BigDecimal monthUpsOrDownsScale;
}
