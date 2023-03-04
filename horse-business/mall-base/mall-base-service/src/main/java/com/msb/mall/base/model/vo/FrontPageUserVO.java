package com.msb.mall.base.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 86151
 */
@Data
public class FrontPageUserVO {
    @ApiModelProperty("今日新增数量")
    private Integer todayIncreaseCount;

    @ApiModelProperty("昨日新增数量")
    private Integer yesterdayIncreaseCount;

    @ApiModelProperty("本月新增数量")
    private Integer monthIncreaseCount;

    @ApiModelProperty("用户总数")
    private Integer allCount;
}
