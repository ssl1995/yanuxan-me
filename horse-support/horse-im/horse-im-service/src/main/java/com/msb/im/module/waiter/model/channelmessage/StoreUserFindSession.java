package com.msb.im.module.waiter.model.channelmessage;

import lombok.Data;

/**
 * 用户侧点击客服创建客服会话
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Data
public class StoreUserFindSession {
    private Long sessionId;
    private Long topMessageId;
    private Long size;
}
