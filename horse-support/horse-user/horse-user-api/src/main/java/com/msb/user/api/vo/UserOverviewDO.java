package com.msb.user.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author shumengjiao
 */
@Data
@Accessors(chain = true)
public class UserOverviewDO implements Serializable {
    @ApiModelProperty("今日新增数量")
    private Integer todayIncreaseCount;

    @ApiModelProperty("昨日新增数量")
    private Integer yesterdayIncreaseCount;

    @ApiModelProperty("本月新增数量")
    private Integer monthIncreaseCount;

    @ApiModelProperty("用户总数")
    private Integer allCount;
}
