package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.convert.MessageConvert;
import com.msb.im.model.entity.Message;
import com.msb.im.model.vo.MessageVO;
import com.msb.im.module.chat.model.FindMessageByMessageIndex;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.module.waiter.service.StoreConfigService;
import com.msb.im.module.waiter.service.StoreWaiterService;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * channel中客服功能用户查询会话中指定消息
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreUserFindMessageByMessageIndexService extends AbstractClientMessageService<FindMessageByMessageIndex> {
    @Resource
    private MessageService messageService;
    @Resource
    private MessageConvert messageConvert;
    @Resource
    private SessionService sessionService;
    @Resource
    private StoreConfigService storeConfigService;
    @Resource
    private StoreWaiterService storeWaiterService;
    @Resource
    private UserConnectService userConnectService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, FindMessageByMessageIndex findMessageByMessageIndex) {
        String userId = getUserId(ctx);
        Integer systemId = getSysId(ctx);
        Long sessionId = findMessageByMessageIndex.getSessionId();
        if (!sessionService.sessionOfUser(sessionId, userId, systemId)) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, null, Collections.emptyList()));
            return false;
        }
        List<Long> messageIds = findMessageByMessageIndex.getMessageIndexIds();
        List<Message> messages = messageService.findDbBySessionIdAndMessageIndexIds(sessionId, messageIds);

        // 客服 用户 店铺信息
        HandshakeParam handshakeParam = channelManager.get(ctx.channel());
        List<StoreWaiter> storeWaiters = storeWaiterService.findBySysId(systemId);
        Map<String, StoreWaiter> waiterMap = storeWaiters.stream().collect(Collectors.toMap(StoreWaiter::getWaiterId, Function.identity()));
        StoreConfig storeConfig = storeConfigService.findBySysId(systemId);

        List<MessageVO> messageVOS = composeMessageVOS(messages, handshakeParam, waiterMap, storeConfig);
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, messageVOS));
        return false;
    }

    private List<MessageVO> composeMessageVOS(List<Message> messages, HandshakeParam handshakeParam, Map<String, StoreWaiter> waiterMap, StoreConfig storeConfig) {
        List<MessageVO> messageVOS = new ArrayList<>(messages.size());
        for (Message message : messages) {
            MessageVO messageVO = messageConvert.toVo(message);
            String fromId = message.getFromId();
            if (fromId == null) {
                messageVO.setFromAvatar(storeConfig.getAvatar());
                messageVO.setFromNickname(storeConfig.getName());
            } else {
                if (!Objects.equals(fromId, handshakeParam.getUser())) {
                    StoreWaiter waiter = waiterMap.get(fromId);
                    if (waiter != null) {
                        messageVO.setFromNickname(waiter.getWaiterNickname());
                        messageVO.setFromAvatar(waiter.getWaiterAvatar());
                    }
                } else {
                    messageVO.setFromAvatar(handshakeParam.getAvatar());
                    messageVO.setFromNickname(handshakeParam.getNickname());
                }
            }
            messageVOS.add(messageVO);
        }
        return messageVOS;
    }

    @Override
    protected boolean paramError(FindMessageByMessageIndex findMessageByMessageIndex) {
        return false;
    }

}
