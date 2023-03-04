package com.msb.im.module.chat.channelmsgservice;

import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.convert.MessageConvert;
import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.model.vo.MessageVO;
import com.msb.im.module.chat.model.FindMessageByMessageIndex;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.module.waiter.service.StoreWaiterService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.service.MessageService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 通过消息索引查询会话中消息处理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Service
@Slf4j
public class WsClientFindMessageByMessageIndexService extends AbstractClientMessageService<FindMessageByMessageIndex> {
    @Resource
    private MessageService messageService;
    @Resource
    private MessageConvert messageConvert;
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private StoreWaiterService storeWaiterService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, FindMessageByMessageIndex findMessageByMessageIndex) {
        String userId = getUserId(ctx);
        Integer systemId = getSysId(ctx);
        Long sessionId = findMessageByMessageIndex.getSessionId();
        if (!sessionService.sessionOfUser(sessionId, userId, systemId)) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "无权限", Collections.emptyList()));
            return false;
        }
        List<Long> messageIds = findMessageByMessageIndex.getMessageIndexIds();
        List<Message> messages = messageService.findDbBySessionIdAndMessageIndexIds(sessionId, messageIds);
        List<MessageVO> messageVOS = composeMessageVOS(messages, systemId, userId);
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, messageVOS));
        return false;
    }

    private List<MessageVO> composeMessageVOS(List<Message> messages, Integer systemId, String userId) {
        // 客服信息
        List<StoreWaiter> storeWaiters = storeWaiterService.findBySysId(systemId);
        Map<String, StoreWaiter> waiterMap = storeWaiters.stream().collect(Collectors.toMap(StoreWaiter::getWaiterId, Function.identity()));
        // 店铺信息
        StoreConfig storeConfig = storeConfigService.findBySysId(systemId);
        List<MessageVO> messageVOS = new ArrayList<>(messages.size());
        for (Message message : messages) {
            MessageVO messageVO = messageConvert.toVo(message);
            setFromUserData(userId, messageVO, waiterMap, storeConfig);
            messageVOS.add(messageVO);
        }
        return messageVOS;
    }

    private void setFromUserData(String userId, MessageVO messageVO, Map<String, StoreWaiter> waiterMap, StoreConfig storeConfig) {
        Session session = sessionService.findSession(messageVO.getSessionId());
        Integer type = session.getType();
        if (Objects.equals(SessionTypeEnum.STORE.getCode(), type)) {
            // 客服会话中消息发送人处理
            if (messageVO.getFromId() == null) {
                // 店铺发的信息
                messageVO.setFromAvatar(storeConfig.getAvatar());
                messageVO.setFromNickname(storeConfig.getName());
            } else {
                if (!Objects.equals(messageVO.getFromId(), userId)) {
                    // 客服发的信息
                    StoreWaiter waiter = waiterMap.get(messageVO.getFromId());
                    if (waiter != null) {
                        messageVO.setFromNickname(waiter.getWaiterNickname());
                        messageVO.setFromAvatar(waiter.getWaiterAvatar());
                    }
                } else {
                    // 用户发的信息
                    SessionUser sessionUser = sessionUserService.findBySessionIdAndUserId(messageVO.getSessionId(), messageVO.getFromId());
                    messageVO.setFromAvatar(sessionUser.getUserAvatar());
                    messageVO.setFromNickname(sessionUser.getUserNickname());
                }
            }
        } else {
            SessionUser sessionUser = sessionUserService.findBySessionIdAndUserId(messageVO.getSessionId(), messageVO.getFromId());
            if (sessionUser != null) {
                messageVO.setFromAvatar(sessionUser.getUserAvatar());
                messageVO.setFromNickname(sessionUser.getUserNickname());
            }
        }
    }

    @Override
    protected boolean paramError(FindMessageByMessageIndex findMessageByMessageIndex) {
        List<Long> messageIndexIds = findMessageByMessageIndex.getMessageIndexIds();
        Long sessionId = findMessageByMessageIndex.getSessionId();
        if (sessionId == null || messageIndexIds.isEmpty()) {
            return true;
        }

        if (sessionId <= 0) {
            return true;
        }

        if (messageIndexIds.stream().anyMatch(messageIndex -> messageIndex <= 0)) {
            return true;
        }
        return false;
    }

}
