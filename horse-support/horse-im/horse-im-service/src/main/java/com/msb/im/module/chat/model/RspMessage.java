package com.msb.im.module.chat.model;

import com.msb.im.netty.ChannelMessageTypeEnum;
import lombok.Data;

import static com.msb.im.module.chat.model.PongMessage.DEFAULT_PONG_MESSAGE;

/**
 * channel中公共响应参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class RspMessage<V> {
    public static final RspMessage<PongMessage> DEFAULT_PONG_RSP_MESSAGE;
    public static final RspMessage<Void> TOKEN_EXPIRE_MESSAGE;
    public static final int ERROR = 500;
    public static final int SUCCESS = 200;
    public static final int TOKEN_EXPIRE = 401;

    static {
        // pong
        DEFAULT_PONG_RSP_MESSAGE = new RspMessage<>();
        DEFAULT_PONG_RSP_MESSAGE.setTraceType(ChannelMessageTypeEnum.PING_PONG.getCode());
        DEFAULT_PONG_RSP_MESSAGE.setContent(DEFAULT_PONG_MESSAGE);

        // token 过期
        TOKEN_EXPIRE_MESSAGE = new RspMessage<>();
        TOKEN_EXPIRE_MESSAGE.setTraceType(ChannelMessageTypeEnum.PUSH_MESSAGE.getCode());
        TOKEN_EXPIRE_MESSAGE.setCode(TOKEN_EXPIRE);
    }

    private String traceId;
    /**
     * {@link ChannelMessageTypeEnum}
     */
    private Integer traceType;
    private Integer code;
    private String message;
    private V content;
}
