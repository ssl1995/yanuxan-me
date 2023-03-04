package com.msb.mall.trade.model.vo.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 销售额统计表(SalesStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:30:19
 */
@Data
public class SalesStatisticsVO implements Serializable {
    
    @ApiModelProperty("记录日期")
    private LocalDate recordDate;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordDateTime;
    
    @ApiModelProperty("销售额")
    private BigDecimal sales;
    
}

