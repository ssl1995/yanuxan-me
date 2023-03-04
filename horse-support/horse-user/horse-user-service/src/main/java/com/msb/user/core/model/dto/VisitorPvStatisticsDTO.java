package com.msb.user.core.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * 访客pv统计表(VisitorPvStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-07 19:40:45
 */
@Data
public class VisitorPvStatisticsDTO implements Serializable {

    @ApiModelProperty("访客统计id")
    private Integer id;
    
    @ApiModelProperty("记录日期")
    private LocalDate recordDate;
    
    @ApiModelProperty("访客数量")
    private Integer count;
    
}

