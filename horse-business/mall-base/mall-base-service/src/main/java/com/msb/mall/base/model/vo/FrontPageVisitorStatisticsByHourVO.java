package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86151
 */
@Data
public class FrontPageVisitorStatisticsByHourVO {
    @ApiModelProperty("时间")
    private LocalDateTime recordDate;

    @ApiModelProperty("访客数量")
    private Integer count;
}
