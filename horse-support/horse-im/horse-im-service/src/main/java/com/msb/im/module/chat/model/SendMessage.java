package com.msb.im.module.chat.model;

import com.msb.im.api.enums.MessageTypeEnum;
import lombok.Data;

/**
 * channel中发送消息参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class SendMessage {
    /**
     * 消息类型 {@link MessageTypeEnum}
     */
    private Integer type;
    private Long toSessionId;
    private String payload;
}
