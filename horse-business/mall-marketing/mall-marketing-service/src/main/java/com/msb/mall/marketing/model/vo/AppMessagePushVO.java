package com.msb.mall.marketing.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * app消息推送(AppMessagePush)表实体类
 *
 * @author makejava
 * @date 2022-04-06 14:11:49
 */
@Data
@Accessors(chain = true)
public class AppMessagePushVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("链接跳转")
    private String linkJump;

    @ApiModelProperty("发布时间")
    private LocalDateTime releaseTime;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新人id")
    private Long updateUser;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("发布人名称")
    private String createUserName;
}

