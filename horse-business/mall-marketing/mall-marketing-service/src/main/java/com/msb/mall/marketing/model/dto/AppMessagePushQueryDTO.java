package com.msb.mall.marketing.model.dto;


import com.msb.framework.common.model.PageDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * app消息推送(AppMessagePush)表实体类
 *
 * @author makejava
 * @date 2022-04-06 14:11:49
 */
@Data
@Accessors(chain = true)
public class AppMessagePushQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("发布时间")
    private LocalDateTime startReleaseTime;

    @ApiModelProperty("发布时间")
    private LocalDateTime endReleaseTime;
}
