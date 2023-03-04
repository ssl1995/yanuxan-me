package com.msb.im.module.chat.model;

import lombok.Data;

/**
 * channel中消息已读参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class AlreadyReadMessage {
    /**
     * 会话id
     */
    private Long sessionId;

}
