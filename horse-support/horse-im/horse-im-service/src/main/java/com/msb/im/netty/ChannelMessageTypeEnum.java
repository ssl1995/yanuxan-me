package com.msb.im.netty;

import com.msb.framework.common.model.IDict;
import com.msb.im.module.chat.channelmsgservice.*;
import com.msb.im.module.chat.channelmsgservice.WsClientFindMySessionService;
import com.msb.im.module.waiter.channelmsgservice.*;

/**
 * channel中传输的消息类型
 * service为null是服务器推送给客户端的消息 不需要处理类
 *
 * @author zhou miao
 * @date 2022/04/18
 */
public enum ChannelMessageTypeEnum implements IDict<Integer> {
    // 通用的收发消息
    PING_PONG(0, "心跳", WsClientPingPongMessageService.class),
    FIND_MY_SESSION(1, "查询我的会话列表", WsClientFindMySessionService.class),
    FIND_SESSION_MESSAGES(2, "查询会话中的消息", WsClientFindSessionMessageService.class),
    SEND_MESSAGE(3, "发送消息", WsClientSendMessageService.class),
    FIND_MESSAGE_BY_IDS(4, "通过消息id查询消息", WsClientFindMessageByMessageIndexService.class),
    SERVER_FORWARD_MESSAGE(5, "服务器转发消息", WsServerForwardMessageService.class),
    MESSAGE_READ(6, "标记消息已读", WsClientAlreadyReadMessageService.class),
    PUSH_MESSAGE(7, "服务器往客户端推送消息", null),
    DELETE_SESSION(8, "删除自己的某个会话", WsClientDeleteSessionService.class),
    CREATE_SESSION(9, "创建会话", WsClientCreateSessionService.class),

    // 客服功能 用户侧消息
    STORE_USER_MESSAGE_READ(17, "标记消息已读", WsStoreUserAlreadyReadMessageService.class),
    STORE_USER_FIND_MESSAGE_BY_IDS(18, "通过消息id查询消息", WsStoreUserFindMessageByMessageIndexService.class),
    STORE_SEND_MESSAGE(20, "用户发送消息", WsStoreUserSendMessageService.class),
    STORE_USER_CREATE_STORE_SESSION(21, "用户创建客服会话", WsStoreUserCreateSessionService.class),
//        STORE_SESSION_USER_PING_PONG(22, "客服会话用户心跳", WsStoreUserPingPongService.class),
    STORE_FIND_USER_SESSION_MESSAGES(23, "用户查询客服会话消息", WsStoreUserFindSessionMessagesService.class),

//    STORE_PUSH_MESSAGE(25, "服务器推送消息", null),

    // 客服功能 客服侧消息,
    STORE_WAITER_PING_PONG(26, "商铺客服心跳", WsStoreWaiterPingPongService.class),
    STORE_WAITER_FIND_SESSIONS(27, "客服查询会话列表", WsStoreWaiterFindMySessionService.class),
    STORE_WAITER_FIND_SESSION_MESSAGES(28, "查询会话消息", WsStoreWaiterFindSessionMessagesService.class),
    STORE_WAITER_READ_MESSAGE(31, "客服消息已读", WsStoreWaiterAlreadyReadMessageService.class),
    STORE_WAITER_SEND_MESSAGE(32, "客服发送消息", WsStoreWaiterSendMessageService.class),

    STORE_TRANSFER_SELECT_WAITER(29, "转移会话查询可以转移的客服", WsStoreWaiterFindOtherWaiterService.class),
    STORE_TRANSFER_WAITER(30, "转移会话", WsStoreWaiterTransferWaiterService.class),
    ;

    Class<? extends AbstractClientMessageService<?>> msgHandleService;

    ChannelMessageTypeEnum(Integer code, String text, Class<? extends AbstractClientMessageService<?>> serviceClazz) {
        init(code, text);
        msgHandleService = serviceClazz;
    }

    public Class<?> getMsgHandleService() {
        return msgHandleService;
    }
}
