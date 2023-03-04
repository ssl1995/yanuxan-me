package com.msb.im.module.chat.model;

import lombok.Data;

/**
 * channel中ping消息参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class PingMessage {

    public static final PingMessage DEFAULT_PING_MESSAGE;

    static {
        DEFAULT_PING_MESSAGE = new PingMessage();
        DEFAULT_PING_MESSAGE.setText("ping");
    }

    private String text;
}
