package com.msb.mall.marketing.model.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Accessors(chain = true)
@Data
public class CurrentActivityTimeVO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("时段名称")
    private String timeName;

    @ApiModelProperty("时段开始时间")
    private LocalTime startTime;

    @ApiModelProperty("时段结束时间")
    private LocalTime endTime;

    @ApiModelProperty("时段开始时间")
    private LocalDateTime activityStartTime;

    @ApiModelProperty("时段结束时间")
    private LocalDateTime activityEndTime;


    @ApiModelProperty("是否正在进行")
    private Boolean isInProgress;
}
