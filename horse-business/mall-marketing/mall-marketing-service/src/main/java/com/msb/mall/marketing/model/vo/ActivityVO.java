package com.msb.mall.marketing.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 活动表(Activity)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Data
@Accessors(chain = true)
public class ActivityVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("活动名称")
    private String name;

    @ApiModelProperty("活动开始时间")
    private LocalDateTime activityStartTime;

    @ApiModelProperty("活动结束时间")
    private LocalDateTime activityEndTime;

    @ApiModelProperty("是否上线")
    private Boolean isOnline;

    @ApiModelProperty("活动时间段")
    private List<ActivityTimeVO> activityTimeList;

    @ApiModelProperty("活动状态 1,未开始 2，进行中 3，已结束")
    public Integer activityState;

    public Integer getActivityState() {
        if (activityStartTime.isAfter(LocalDateTime.now())) {
            return 1;
        }
        if (activityEndTime.isBefore(LocalDateTime.now())) {
            return 3;
        }
        return 2;
    }
}

