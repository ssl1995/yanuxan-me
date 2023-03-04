package com.msb.mall.marketing.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 活动表(Activity)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Data
@Accessors(chain = true)
public class SaveActivityDTO implements Serializable {

    @Size(max = 30)
    @NotBlank
    @ApiModelProperty("活动名称")
    private String name;

    @NotNull
    @ApiModelProperty("活动开始时间")
    private LocalDate activityStartTime;

    @NotNull
    @ApiModelProperty("活动结束时间")
    private LocalDate activityEndTime;

    @NotNull
    @ApiModelProperty("是否上线")
    private Boolean isOnline;
}

