package com.msb.im.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话VO
 *
 * @author zhou miao
 * @date 2022/04/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionVO {
    private Long id;
    private Integer sysId;
    private Integer type;
    private String payload;
    /**
     * 群会话头像
     */
    private String groupAvatar;
    /**
     * 群会话名称
     */
    private String groupName;
    /**
     * 单人会话对面头像
     */
    private String fromAvatar;
    /**
     * 单人会话对面发送人id
     */
    private String fromId;
    /**
     * 单人会话对面发送人昵称
     */
    private String fromNickname;
    /**
     * 单人会话对面发送人性别 1-男2-女3-未知
     */
    private Integer fromSex;
    private Long unreadCount;
    private Long updateTimeStamp;
    private MessageVO lastMessage;
}
