package com.msb.user.core.model.dto;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;


/**
 * 访客统计表(VisitorStatistics)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-07 12:55:16
 */
@Data
public class VisitorUvStatisticsDTO implements Serializable {

    @ApiModelProperty("访客统计id")
    private Integer id;
    
    @ApiModelProperty("记录日期")
    private LocalDate recordDate;
    
    @ApiModelProperty("访客数量")
    private Integer count;
    
}

