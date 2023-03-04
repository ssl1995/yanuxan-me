package com.msb.mall.trade.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author shumengjiao
 */
@Data
public class SalesStatisticsByDayDO implements Serializable {
    @ApiModelProperty("时间")
    private LocalDate recordDate;

    @ApiModelProperty("销售额")
    private BigDecimal sales;
}
