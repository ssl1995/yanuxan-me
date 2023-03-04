package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.convert.MessageConvert;
import com.msb.im.model.entity.Message;
import com.msb.im.model.vo.MessageVO;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.bo.WaiterBO;
import com.msb.im.module.waiter.model.channelmessage.StoreUserFindSession;
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
 * channel中客服功能用户查询会话消息列表
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreUserFindSessionMessagesService extends AbstractClientMessageService<StoreUserFindSession> {
    @Resource
    private SessionService sessionService;
    @Resource
    private MessageService messageService;
    @Resource
    private MessageConvert messageConvert;
    @Resource
    private StoreWaiterService storeWaiterService;
    @Resource
    private StoreConfigService storeConfigService;
    @Resource
    private UserConnectService userConnectService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreUserFindSession storeUserFindSession) {
        Long sessionId = storeUserFindSession.getSessionId();
        Long topMessageId = storeUserFindSession.getTopMessageId();
        Long size = storeUserFindSession.getSize();
        String userId = getUserId(ctx);
        Integer sysId = getSysId(ctx);

        if (!sessionService.sessionOfUser(sessionId, userId, sysId)) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "无权限", Collections.emptyList()));
            return true;
        }

        List<Message> messages = messageService.findSessionMessages(sessionId, userId, topMessageId, size);
        if (messages.isEmpty()) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, Collections.emptyList()));
            return false;
        }

        HandshakeParam handshakeParam = channelManager.get(ctx.channel());

        // 批量查询客服昵称
        List<StoreWaiter> storeWaiters = storeWaiterService.findBySysId(sysId);
        // 查询店铺配置
        StoreConfig storeConfig = storeConfigService.findBySysId(sysId);

        List<MessageVO> messageVOS = composeMessageVos(messages, handshakeParam, storeWaiters, storeConfig);
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, messageVOS));
        return false;
    }

    private List<MessageVO> composeMessageVos(List<Message> messages, HandshakeParam handshakeParam, List<StoreWaiter> storeWaiters, StoreConfig storeConfig) {
        Map<String, StoreWaiter> waiterMap = storeWaiters.stream().collect(Collectors.toMap(StoreWaiter::getWaiterId, Function.identity()));
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

    private Map<String, WaiterBO> convert2Map(List<WaiterBO> waiters) {
        return waiters.stream().collect(Collectors.toMap(WaiterBO::getUserId, Function.identity()));
    }

    /**
     * 参数校验
     *
     * @param storeUserFindSession
     */
    @Override
    public boolean paramError(StoreUserFindSession storeUserFindSession) {
        Long sessionId = storeUserFindSession.getSessionId();
        if (sessionId == null) {
            return true;
        }
        return false;
    }


}
