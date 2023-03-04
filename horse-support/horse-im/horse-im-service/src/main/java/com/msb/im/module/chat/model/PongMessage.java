package com.msb.im.module.chat.model;

import lombok.Data;

/**
 * channel中pong消息参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class PongMessage {
    public static final PongMessage DEFAULT_PONG_MESSAGE;

    static {
        DEFAULT_PONG_MESSAGE = new PongMessage();
        DEFAULT_PONG_MESSAGE.setText("pong");
    }
    private String text;
}
