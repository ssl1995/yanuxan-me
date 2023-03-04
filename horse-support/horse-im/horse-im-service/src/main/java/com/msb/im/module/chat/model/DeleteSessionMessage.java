package com.msb.im.module.chat.model;

import lombok.Data;

/**
 * 删除会话参数
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Data
public class DeleteSessionMessage {
    /**
     * 会话id
     */
    private Long sessionId;

}
