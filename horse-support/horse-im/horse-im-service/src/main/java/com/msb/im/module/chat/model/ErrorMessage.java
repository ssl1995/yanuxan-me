package com.msb.im.module.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * channel中响应的错误消息体
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private String msg;
}
