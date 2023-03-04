package com.msb.im.module.waiter.model.bo;

import com.msb.im.api.enums.MessageTypeEnum;
import lombok.Data;

/**
 * 往客服会话中发送的消息体
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class SendStoreMessageBO {
    /**
     * 消息类型 {@link MessageTypeEnum}
     */
    private Integer type;
    private Long fromId;
    private Long toId;
    private Long sessionId;
    private Long storeId;
    private Long waiterId;
    private String payload;
    private Integer sysId;
    private String traceId;
    private Integer traceType;

}
