package com.msb.im.model.bo;

import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.model.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 发送消息BO
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageBO {
    /**
     * 往会话中发送一条消息 并通知会话中所有人时使用
     *
     * 消息类型 type {@link MessageTypeEnum}
     */
    private MessageTypeEnum type;
    private String fromId;
    private String fromAvatar;
    private String fromNickname;
    private String toId;
    private Long toSessionId;
    private String payload;
    private Integer sysId;
    private String toAvatar;
    private String toNickname;
    /**
     * 会话自定义信息
     */
    private String sessionPayload;

    /**
     * 会话类型
     */
    private SessionTypeEnum sessionTypeEnum;


    /**
     * 往会话中发送一条消息 通知指定人时使用 自定义会话的头标和昵称使用
     */
    private Set<String> toUserIds;
    private Set<String> addUnreadUserIds;
    private Message message;

    /**
     * 通用字段
     */
    private String traceId;
    private Integer traceType;

}
