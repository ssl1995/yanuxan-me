package com.msb.im.module.chat.model;

import com.msb.im.netty.ChannelMessageTypeEnum;
import lombok.Data;

import static com.msb.im.module.chat.model.PingMessage.DEFAULT_PING_MESSAGE;

/**
 * channel中公共请求参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class ReqMessage<V> {
    public static final ReqMessage<PingMessage> DEFAULT_PING_REQ_MESSAGE;
    static {
        // ping
        DEFAULT_PING_REQ_MESSAGE = new ReqMessage<>();
        DEFAULT_PING_REQ_MESSAGE.setTraceType(ChannelMessageTypeEnum.PING_PONG.getCode());
        DEFAULT_PING_REQ_MESSAGE.setContent(DEFAULT_PING_MESSAGE);
    }

    private String traceId;
    /**
     * {@link ChannelMessageTypeEnum}
     */
    private Integer traceType;
    private V content;
}
