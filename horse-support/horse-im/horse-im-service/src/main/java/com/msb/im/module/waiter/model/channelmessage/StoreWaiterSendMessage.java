package com.msb.im.module.waiter.model.channelmessage;

import com.msb.im.api.enums.MessageTypeEnum;
import lombok.Data;

/**
 * 客服发送消息体
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class StoreWaiterSendMessage {
    /**
     * 消息类型 {@link MessageTypeEnum}
     */
    private Integer type;
    private Long toSessionId;
    private String payload;
    private String traceId;
    private Integer traceType;
    private String fromId;
    private Long storeId;
    private Integer sysId;

}
