package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 86151
 */
@Data
public class FrontPageOrderStatisticsByHourVO {
    @ApiModelProperty("时间")
    private Integer recordDate;

    @ApiModelProperty("订单数量")
    private Integer count;
}
