package com.msb.mall.trade.model.vo.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 订单统计表(OrderStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:17:31
 */
@Data
public class OrderStatisticsVO implements Serializable {

    @ApiModelProperty("记录日期")
    private LocalDate recordDate;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordDateTime;

    @ApiModelProperty("数量")
    private Integer count;

}

