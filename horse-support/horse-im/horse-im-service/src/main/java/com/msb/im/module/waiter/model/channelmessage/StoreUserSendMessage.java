package com.msb.im.module.waiter.model.channelmessage;

import com.msb.im.netty.model.HandshakeParam;
import lombok.Data;

/**
 * 客服功能用户发送消息
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Data
public class StoreUserSendMessage {
    /**
     * 前端传递的参数
     */
    private Integer type;
    private Long toSessionId;
    private String payload;

    /**
     * 后台自己可以查到的参数，不需要前端传递
     */
    private String traceId;
    private Integer traceType;
    private Integer sysId;
    private String fromId;
    private Long storeId;
    private HandshakeParam handshakeParam;

}
