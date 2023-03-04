package com.msb.user.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author smj
 */
@Data
public class VisitorStatisticsByDayDO implements Serializable {
    @ApiModelProperty("日期")
    private LocalDate recordDate;

    @ApiModelProperty("访客数量")
    private Integer count;
}
