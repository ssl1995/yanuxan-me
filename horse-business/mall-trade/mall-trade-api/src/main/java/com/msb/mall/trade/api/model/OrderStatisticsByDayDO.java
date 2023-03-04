package com.msb.mall.trade.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author shumengjiao
 */
@Data
public class OrderStatisticsByDayDO implements Serializable {

    @ApiModelProperty("时间")
    private LocalDate recordDate;

    @ApiModelProperty("订单数量")
    private Integer count;
}
