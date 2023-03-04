package com.msb.im.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (HorseImSession)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:19
 */
@Data
@TableName("session")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session implements Serializable {

    @TableId
    private Long id;

    /**
     * 1-严选商城
     */
    private Integer sysId;

    /**
     * 1-单聊会话 2-群聊会话
     * {@link com.msb.im.api.enums.SessionTypeEnum}
     */
    private Integer type;

    /**
     * 群头像
     */
    private String groupAvatar;

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 会话元数据
     */
    private String payload;

    @TableLogic
    private Boolean isDeleted;

    private Long createTimeStamp;
    private Long updateTimeStamp;

    /**
     * 创建人（id）
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    /**
     * 更新人（id）
     */
//    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;


}

