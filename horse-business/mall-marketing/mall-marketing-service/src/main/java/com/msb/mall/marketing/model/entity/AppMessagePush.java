package com.msb.mall.marketing.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * app消息推送(AppMessagePush)表实体类
 *
 * @author makejava
 * @date 2022-04-06 14:11:49
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_message_push")
public class AppMessagePush extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 链接跳转
     */
    private String linkJump;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
