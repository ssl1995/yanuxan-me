package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author 86151
 */
@Data
public class FrontPageSalesStatisticsByHourVO {
    @ApiModelProperty("时间")
    private Integer recordDate;

    @ApiModelProperty("销售额")
    private BigDecimal sales;
}
