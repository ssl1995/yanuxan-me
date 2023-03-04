package com.msb.mall.marketing.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;

/**
 * 活动时段表(ActivityTime)表实体类
 *
 * @author makejava
 * @date 2022-04-08 14:57:59
 */
@Data
@Accessors(chain = true)
public class ActivityTimeDTO implements Serializable {

    @NotNull
    @ApiModelProperty("活动表id")
    private Long activityId;

    @NotNull
    @ApiModelProperty("时段名称")
    private String timeName;

    @NotNull
    @ApiModelProperty("时段开始时间 (时间格式 HH:mm:ss)")
    private LocalTime startTime;

    @NotNull
    @ApiModelProperty("时段结束时间")
    private LocalTime endTime;

    @NotNull
    @ApiModelProperty("是否启用")
    private Boolean isEnable;
}

