package com.msb.im.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * (HorseImMessage)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:57
 */
@Data
public class MessageDTO implements Serializable {

    private Integer id;

    @ApiModelProperty("消息发送人")
    private Long fromId;

    @ApiModelProperty("消息在会话中的id 在当前会话中是连续的")
    private Integer sessionMessageId;

    @ApiModelProperty("会话id")
    private Integer sessionId;

    @ApiModelProperty("消息类型 1-文本 2-语音 3-图片 4-视频")
    private Integer type;

    @ApiModelProperty("发送的消息体")
    private String payload;

    @ApiModelProperty("消息创建的时间戳")
    private Long createTimeStamp;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    @ApiModelProperty("是否删除 1-是 0-否")
    private Boolean isDeleted;

}

