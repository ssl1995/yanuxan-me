package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 86151
 */
@Data
public class FrontPageOrderStatisticsByDayVO {
    @ApiModelProperty("日期")
    private LocalDate recordDate;

    @ApiModelProperty("订单数量")
    private Integer count;
}
