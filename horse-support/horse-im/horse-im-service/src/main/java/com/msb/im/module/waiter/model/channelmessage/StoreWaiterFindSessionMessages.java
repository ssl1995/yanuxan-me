package com.msb.im.module.waiter.model.channelmessage;

import lombok.Data;

/**
 * 客服查询会话中消息
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Data
public class StoreWaiterFindSessionMessages {
    private Long sessionId;
    private Long topMessageId;
    private Long size;
    private String traceId;
    private Integer traceType;
    private Long storeId;
}
