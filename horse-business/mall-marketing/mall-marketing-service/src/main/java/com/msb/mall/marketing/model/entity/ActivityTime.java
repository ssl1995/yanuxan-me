package com.msb.mall.marketing.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * 活动时段表(ActivityTime)表实体类
 *
 * @author makejava
 * @date 2022-04-08 14:57:59
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("activity_time")
public class ActivityTime extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动表id
     */
    private Long activityId;

    /**
     * 时段名称
     */
    private String timeName;

    /**
     * 时段开始时间
     */
    private LocalTime startTime;

    /**
     * 时段结束时间
     */
    private LocalTime endTime;

    /**
     * 是否启用
     */
    private Boolean isEnable;


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;


}
