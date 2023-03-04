package com.msb.im.mq.model;

import com.msb.im.mq.service.SendAndInsertSessionImMqMessageHandleService;
import com.msb.im.mq.service.SendAndUpdateSessionImMqMessageHandleService;
import com.msb.im.mq.service.UpdateUnReadImMqMessageHandleService;

/**
 * 消息类型有：
 * 发送消息
 * 消息撤回
 * 更新未读
 * 会话置顶
 * 删除会话用户
 * 删除会话
 * 不同的消息有不同的处理对象 不同的处理逻辑
 *
 * @author zhou miao
 * @date 2022/04/29
 */
public enum ImMqMessageTypeEnum {
    //
    SEND_AND_INSERT_SESSION_MESSAGE(SendAndInsertSessionImMqMessageHandleService.class),
    SEND_AND_UPDATE_SESSION_MESSAGE(SendAndUpdateSessionImMqMessageHandleService.class),
    UPDATE_UNREAD(UpdateUnReadImMqMessageHandleService.class),
    // 以下暂未实现
    WITHDRAW_MESSAGE(null),
    TOP_MESSAGE(null),
    DELETE_SESSION_USER(null),
    DELETE_SESSION(null),
    ;

    Class<?> handleService;

    ImMqMessageTypeEnum(Class<?> handleService) {
        this.handleService = handleService;
    }

    public Class<?> getHandleService() {
        return handleService;
    }
}
