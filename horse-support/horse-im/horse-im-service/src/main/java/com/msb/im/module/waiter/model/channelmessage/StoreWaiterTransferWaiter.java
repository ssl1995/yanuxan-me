package com.msb.im.module.waiter.model.channelmessage;

import lombok.Data;

/**
 * 客服转移会话
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class StoreWaiterTransferWaiter {
    private Long storeId;
    private Long sessionId;
    private String toWaiterId;
    private String reason;
    private String currentWaiterId;
    private String traceId;
    private Integer traceType;
    private Integer sysId;
}
