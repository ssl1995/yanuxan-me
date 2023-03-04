package com.msb.im.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (HorseImSessionUser)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:34
 */
@Data
@TableName("session_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionUser implements Serializable {

    @TableId
    private Long id;

    private Long sessionId;

    private String userId;

    /**
     * 单人会话中会话对面人id
     */
    private String relationUserId;

    /**
     * 未读消息数量
     */
    private Long unReadCount;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 消息未读开始索引
     */
//    private Long messageIndexUnreadStart;

    /**
     * 消息未读结束索引
     */
//    private Long messageIndexUnreadEnd;

    private Boolean isGroupOwner;

    private Long createTimeStamp;
    private Long updateTimeStamp;

    @TableLogic
    private Boolean isDeleted;

    /**
     * 创建人（id）
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    /**
     * 更新人（id）
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;

}

