package com.msb.im.model.vo;

import com.msb.im.api.enums.MessageTypeEnum;
import lombok.Data;

import java.util.Set;

/**
 * 消息VO
 *
 * @author zhou miao
 * @date 2022/04/21
 */
@Data
public class MessageVO {

    private Integer id;

    private String fromId;
    private String fromNickname;
    private String fromAvatar;

    // 消息在会话中的连续id
    private Long messageIndex;

    private Long sessionId;

    /**
     * 消息类型
     * {@link MessageTypeEnum}
     */
    private Integer type;

    /**
     * 客户端发的消息体
     */
    private String payload;

    private Long createTimeStamp;

    /**
     * 消息是否已读
     */
    private Boolean isRead;



    // 该消息的会话信息
    private SessionVO session;

    // 服务器内部转发使用
    private String originTraceId;
    // 服务器内部转发使用
    private Integer originTraceType;
    // 转发的连接通道类型
    /**
     * {@link com.msb.im.netty.ConnectTypeEnum}
     */
    private String connectCode;
    private String toId;
    private Long storeId;

}
