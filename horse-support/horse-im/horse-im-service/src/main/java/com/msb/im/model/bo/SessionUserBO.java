package com.msb.im.model.bo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (HorseImSessionUser)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:34
 */
@Data
public class SessionUserBO implements Serializable {

    private Long id;

    private Long sessionId;

    private String userId;

    /**
     * 未读消息数量
     */
    private Long unReadCount;

    private Boolean isGroupOwner;

    private Long updateTimeStamp;

    private Boolean isDeleted;

    private Integer type;


    /**
     * 创建人（id）
     */
    private Long createUser;
    /**
     * 更新人（id）
     */
    private Long updateUser;

    private String sessionAvatar;
    private String sessionNickname;

}

