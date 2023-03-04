package com.msb.im.module.waiter.model.channelmessage;

import lombok.Data;

/**
 * 用户在商铺中维护心跳的消息体
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Data
public class StoreUserPingPong {
    private Long storeId;
}
