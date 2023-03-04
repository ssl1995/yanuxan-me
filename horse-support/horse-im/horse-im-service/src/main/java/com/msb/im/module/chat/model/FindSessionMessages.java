package com.msb.im.module.chat.model;

import lombok.Data;

/**
 * channel中查询会话消息参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class FindSessionMessages {
    private Long sessionId;
    private Long topMessageId;
    private Long size;

}
