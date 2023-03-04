package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author 86151
 */
@Data
public class FrontPageSalesStatisticsByDayVO {
    @ApiModelProperty("时间")
    private LocalDate recordDate;

    @ApiModelProperty("销售额")
    private BigDecimal sales;
}
