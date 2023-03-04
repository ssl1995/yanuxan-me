package com.msb.mall.trade.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author shumengjiao
 */
@Data
@Accessors(chain = true)
public class OrderStatisticsByHourDO implements Serializable {
    @ApiModelProperty("小时")
    private Integer recordDate;

    @ApiModelProperty("订单数量")
    private Integer count;
}
