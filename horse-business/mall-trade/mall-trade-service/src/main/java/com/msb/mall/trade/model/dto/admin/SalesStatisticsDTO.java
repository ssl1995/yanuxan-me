package com.msb.mall.trade.model.dto.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * 销售额统计表(SalesStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:30:19
 */
@Data
public class SalesStatisticsDTO implements Serializable {

    @ApiModelProperty("记录日期-开始区间")
    @NotNull
    private LocalDate recordDateBegin;

    @ApiModelProperty("记录日期-结束区间")
    @NotNull
    private LocalDate recordDateEnd;
    
}

