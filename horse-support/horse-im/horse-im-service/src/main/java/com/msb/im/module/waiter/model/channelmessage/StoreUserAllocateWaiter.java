package com.msb.im.module.waiter.model.channelmessage;

import lombok.Data;

/**
 * 客户端请求一个客服
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Data
public class StoreUserAllocateWaiter {
    private String storeId;
    private String userId;
    private Integer sysId;
    private String traceId;
    private Integer traceTpe;
    private Long sessionId;
}
