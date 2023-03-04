package com.msb.mall.marketing.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import com.msb.framework.web.result.BizAssert;
import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 活动表(Activity)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("activity")
public class Activity extends BaseEntity implements Serializable {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime activityEndTime;

    /**
     * 是否上线
     */
    private Boolean isOnline;


    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;


    public void setActivityEndTime(LocalDateTime activityEndTime) {
        this.activityEndTime = activityEndTime;
        checkActivityTime();
    }

    public void setActivityStartTime(LocalDateTime activityStartTime) {
        this.activityStartTime = activityStartTime;
        checkActivityTime();
    }

    private void checkActivityTime() {
        if (Objects.nonNull(activityEndTime) && Objects.nonNull(activityStartTime)) {
            BizAssert.isTrue(activityEndTime.isAfter(activityStartTime), "开始时间不能晚于结束时间");
        }
    }
}
