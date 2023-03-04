package com.msb.im.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (HorseImMessage)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
@Data
@TableName("message")
public class Message implements Serializable {

    @TableId
    private Long id;

    /**
     * 消息发送人
     */
    private String fromId;

    /**
     * 消息在会话中的id 在当前会话中是连续的
     */
    private Long messageIndex;

    /**
     * 会话id
     */
    private Long sessionId;

    /**
     * 消息类型 1-文本 2-语音 3-图片 4-视频
     */
    private Integer type;

    /**
     * 发送的消息体
     */
    private String payload;

    /**
     * 消息创建的时间戳
     */
    private Long createTimeStamp;


    /**
     * 是否删除 1-是 0-否
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}

