package com.msb.mall.trade.model.dto.admin;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;


/**
 * 订单统计表(OrderStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:17:31
 */
@Data
public class OrderStatisticsDTO implements Serializable {
    
    @ApiModelProperty("记录日期-开始区间")
    @NotNull
    private LocalDate recordDateBegin;

    @ApiModelProperty("记录日期-结束区间")
    @NotNull
    private LocalDate recordDateEnd;
    
}

