package com.msb.im.module.waiter.model.channelmessage;

import lombok.Data;

/**
 * 客服已读消息回调消息体
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Data
public class StoreWaiterAlreadyReadMessage {
    private String traceId;
    private Integer traceType;
    private String waiterId;
    private Long storeId;
    private Long sessionId;
}
