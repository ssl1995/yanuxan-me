package com.msb.user.core.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户统计VO
 * @author shumengjiao
 * @date 2022-05-30
 */
@Data
public class UserStatisticsVO implements Serializable {

    @ApiModelProperty("用户总数")
    private Integer userCount;

    @ApiModelProperty("今日新增用户数量")
    private Integer todayAdditions;

    @ApiModelProperty("昨日新增用户数量")
    private Integer yesterdayAdditions;

    @ApiModelProperty("本月新增用户数量")
    private Integer thisMonthAdditions;
}
