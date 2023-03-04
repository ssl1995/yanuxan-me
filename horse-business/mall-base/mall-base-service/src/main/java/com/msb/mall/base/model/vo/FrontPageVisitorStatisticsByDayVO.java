package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 86151
 */
@Data
public class FrontPageVisitorStatisticsByDayVO {
    @ApiModelProperty("日期")
    private LocalDate recordDate;

    @ApiModelProperty("访客数量")
    private Integer count;
}
