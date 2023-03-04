package com.msb.im.module.chat.channelmsgservice;

import com.msb.framework.common.model.IDict;
import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.model.bo.SendMessageBO;
import com.msb.im.model.entity.Session;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.chat.model.SendMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreUserSendMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterSendMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.netty.model.HandshakeParam;
import com.msb.im.netty.service.UserConnectService;
import com.msb.im.service.MessageService;
import com.msb.im.service.SessionService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 心跳消息处理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Service
@Slf4j
public class WsClientSendMessageService extends AbstractClientMessageService<SendMessage> {
    @Resource
    private MessageService messageService;
    @Resource
    private UserConnectService userConnectService;
    @Resource
    private SessionService sessionService;
    @Resource
    private StoreService storeService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, SendMessage sendMessage) {
        boolean waiterConnect = channelManager.isWaiterConnect(ctx.channel());
        if (waiterConnect) {
            StoreWaiterSendMessage storeWaiterSendMessage = new StoreWaiterSendMessage();
            storeWaiterSendMessage.setToSessionId(sendMessage.getToSessionId());
            storeWaiterSendMessage.setType(sendMessage.getType());
            storeWaiterSendMessage.setPayload(sendMessage.getPayload());
            storeWaiterSendMessage.setFromId(getStoreWaiterId(ctx));
            storeWaiterSendMessage.setStoreId(getStoreId(ctx));
            storeWaiterSendMessage.setTraceId(traceId);
            storeWaiterSendMessage.setTraceType(traceType);
            storeWaiterSendMessage.setSysId(getStoreSysId(ctx));
            storeService.waiterSendMessage(storeWaiterSendMessage);
        } else {
            Integer sysId = getSysId(ctx);
            String userId = getUserId(ctx);
            if (!sessionService.sessionOfUser(sendMessage.getToSessionId(), getUserId(ctx), sysId)) {
                ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "你没有该会话权限", null));
                return false;
            }
            SendMessageBO sendMessageBO = convertBO(ctx, traceId, traceType, sendMessage);
            Long toSessionId = sendMessage.getToSessionId();
            Session session = sessionService.findSession(toSessionId);
            if (Objects.equals(SessionTypeEnum.STORE.getCode(), session.getType())) {
                storeUserSendMessage(ctx, traceId, traceType, sendMessage, sysId, userId);
            } else if (Objects.equals(SessionTypeEnum.SINGLE.getCode(), session.getType())) {
                messageService.sendSessionExistSingleMessage(sendMessageBO);
            }
        }
        return false;
    }

    private void storeUserSendMessage(ChannelHandlerContext ctx, String traceId, Integer traceType, SendMessage sendMessage, Integer sysId, String userId) {
        StoreConfig storeConfig = storeConfigService.findBySysId(sysId);
        if (storeConfig == null) {
            log.warn("无权限调用 {}", sysId);
            return;
        }
        StoreUserSendMessage storeUserSendMessage = new StoreUserSendMessage();
        storeUserSendMessage.setType(sendMessage.getType());
        storeUserSendMessage.setToSessionId(sendMessage.getToSessionId());
        storeUserSendMessage.setPayload(sendMessage.getPayload());
        storeUserSendMessage.setSysId(sysId);
        storeUserSendMessage.setFromId(userId);
        storeUserSendMessage.setStoreId(storeConfig.getId());
        storeUserSendMessage.setTraceId(traceId);
        storeUserSendMessage.setTraceType(traceType);
        HandshakeParam handshakeParam = channelManager.get(ctx.channel());
        storeUserSendMessage.setHandshakeParam(handshakeParam);
        storeService.userSendMessage(storeUserSendMessage);
    }

    private SendMessageBO convertBO(ChannelHandlerContext ctx, String traceId, Integer traceType, SendMessage sendMessage) {
        String userId = getUserId(ctx);
        int sysId = getSysId(ctx);
        HandshakeParam handshakeParam = channelManager.get(ctx.channel());
        Session session = sessionService.findSession(sendMessage.getToSessionId());
        SessionTypeEnum sessionTypeEnum = IDict.getByCode(SessionTypeEnum.class, session.getType());
        Long toSessionId = sendMessage.getToSessionId();
        return SendMessageBO.builder()
                .type(IDict.getByCode(MessageTypeEnum.class, sendMessage.getType()))
                .toSessionId(toSessionId)
                .sysId(sysId)
                .fromId(userId)
                .fromAvatar(handshakeParam.getAvatar())
                .fromNickname(handshakeParam.getNickname())
                .payload(sendMessage.getPayload())
                .traceId(traceId)
                .traceType(traceType)
                .sessionTypeEnum(sessionTypeEnum)
                .build();
    }

    /**
     * 参数校验
     *
     * @param sendMessage
     */
    @Override
    public boolean paramError(SendMessage sendMessage) {
        if (sendMessage.getType() == null || sendMessage.getPayload() == null) {
            return true;
        }

        if (!MessageTypeEnum.ofCode(sendMessage.getType())) {
            return true;
        }

        return sendMessage.getToSessionId() == null;
    }


}
