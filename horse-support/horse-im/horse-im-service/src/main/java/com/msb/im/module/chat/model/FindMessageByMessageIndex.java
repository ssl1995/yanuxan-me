package com.msb.im.module.chat.model;

import lombok.Data;

import java.util.List;

/**
 * channel中查询会话总消息的消息体
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class FindMessageByMessageIndex {
    private List<Long> messageIndexIds;
    private Long sessionId;
}
