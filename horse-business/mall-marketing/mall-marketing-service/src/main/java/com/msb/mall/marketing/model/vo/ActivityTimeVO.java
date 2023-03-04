package com.msb.mall.marketing.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 活动时段表(ActivityTime)表实体类
 *
 * @author makejava
 * @date 2022-04-08 14:57:59
 */
@Data
@Accessors(chain = true)
public class ActivityTimeVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("活动表id")
    private Long activityId;

    @ApiModelProperty("时段名称")
    private String timeName;

    @ApiModelProperty("时段开始时间")
    private LocalTime startTime;

    @ApiModelProperty("时段结束时间")
    private LocalTime endTime;

    @ApiModelProperty("活动时间开始时间")
    private LocalDateTime activityStartTime;

    @ApiModelProperty("活动时间时段结束时间")
    private LocalDateTime activityEndTime;

    @ApiModelProperty("活动状态 1，未开始，2，进行中 ")
    private Integer state;

    @ApiModelProperty("是否启用")
    private Boolean isEnable;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新人id")
    private Long updateUser;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("时间段内的秒杀商品id")
    private List<Long> productIdList;
}

