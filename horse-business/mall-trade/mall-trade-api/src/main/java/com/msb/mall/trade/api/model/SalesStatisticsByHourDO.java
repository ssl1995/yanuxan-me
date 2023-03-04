package com.msb.mall.trade.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author shumengjiao
 */
@Data
@Accessors(chain = true)
public class SalesStatisticsByHourDO implements Serializable {
    @ApiModelProperty("小时")
    private Integer recordDate;

    @ApiModelProperty("销售额")
    private BigDecimal sales;
}
