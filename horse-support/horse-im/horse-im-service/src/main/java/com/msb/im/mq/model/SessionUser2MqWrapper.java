package com.msb.im.mq.model;

import com.msb.im.model.entity.SessionUser;
import lombok.Data;

/**
 * mq中会话用户消费消息体
 *
 * @author zhou miao
 * @date 2022/04/30
 */
@Data
public class SessionUser2MqWrapper {
    private SessionUser sessionUser;
}
