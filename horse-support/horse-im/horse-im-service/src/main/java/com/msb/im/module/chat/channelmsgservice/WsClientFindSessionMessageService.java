package com.msb.im.module.chat.channelmsgservice;

import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.model.entity.Session;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import com.msb.im.module.waiter.service.StoreWaiterService;
import com.msb.im.util.RspFrameUtil;
import com.msb.im.convert.MessageConvert;
import com.msb.im.model.entity.Message;
import com.msb.im.model.entity.SessionUser;
import com.msb.im.model.vo.MessageVO;
import com.msb.im.module.chat.model.FindSessionMessages;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.service.MessageService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 查询会话中的消息处理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Service
@Slf4j
public class WsClientFindSessionMessageService extends AbstractClientMessageService<FindSessionMessages> {
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
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, FindSessionMessages findSessionMessages) {
        log.info("查询会话中的消息 {}", findSessionMessages);
        String userId;
        int systemId;
        Long sessionId = findSessionMessages.getSessionId();
        boolean waiterConnect = channelManager.isWaiterConnect(ctx.channel());
        if (waiterConnect) {
            userId = getStoreId(ctx).toString();
            systemId = getStoreSysId(ctx);
        } else {
            userId = getUserId(ctx);
            systemId = getSysId(ctx);
        }
        if (!sessionService.sessionOfUser(sessionId, userId, systemId)) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "无会话权限", Collections.emptyList()));
            return true;
        }

        List<Message> messages = messageService.findSessionMessages(findSessionMessages.getSessionId(), userId, findSessionMessages.getTopMessageId(), findSessionMessages.getSize());
        if (messages.isEmpty()) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, Collections.emptyList()));
            return false;
        }

        Session session = sessionService.findSession(sessionId);
        boolean isStoreSession = Objects.equals(SessionTypeEnum.STORE.getCode(), session.getType());

        List<MessageVO> messageVOS = composeMessageVOS(messages, systemId, userId, isStoreSession, waiterConnect);
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, messageVOS));
        return false;
    }

    private List<MessageVO> composeMessageVOS(List<Message> messages, Integer systemId, String userId, boolean isStoreSession, boolean waiterConnect) {
        Map<String, StoreWaiter> waiterMap = null;
        StoreConfig storeConfig = null;
        if (isStoreSession) {
            // 客服信息
            List<StoreWaiter> storeWaiters = storeWaiterService.findBySysId(systemId);
            waiterMap = storeWaiters.stream().collect(Collectors.toMap(StoreWaiter::getWaiterId, Function.identity()));
            // 店铺信息
            storeConfig = storeConfigService.findBySysId(systemId);
        }
        List<MessageVO> messageVOS = new ArrayList<>(messages.size());
        for (Message message : messages) {
            MessageVO messageVO = messageConvert.toVo(message);
            setFromUserData(userId, messageVO, waiterMap, storeConfig, isStoreSession, waiterConnect);
            messageVOS.add(messageVO);
        }
        return messageVOS;
    }

    private void setFromUserData(String userId, MessageVO messageVO, Map<String, StoreWaiter> waiterMap, StoreConfig storeConfig, boolean isStoreSession, boolean waiterConnect) {
        if (isStoreSession) {
            if (messageVO.getFromId() == null) {
                // 店铺发的信息
                messageVO.setFromAvatar(storeConfig.getAvatar());
                messageVO.setFromNickname(storeConfig.getName());
            } else {
                if (waiterConnect) {
                    if (waiterMap.containsKey(messageVO.getFromId())) {
                        // 客服发的信息
                        setWaiterData(messageVO, waiterMap);
                    } else {
                        // 用户发的信息
                        setUserData(messageVO);
                    }
                } else {
                    if (!Objects.equals(messageVO.getFromId(), userId)) {
                        // 客服发的信息
                        setWaiterData(messageVO, waiterMap);
                    } else {
                        // 用户发的信息
                        setUserData(messageVO);
                    }
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

    private void setUserData(MessageVO messageVO) {
        SessionUser sessionUser = sessionUserService.findBySessionIdAndUserId(messageVO.getSessionId(), messageVO.getFromId());
        if (sessionUser == null) {
            log.warn("会话用户信息不存在 {} {}", messageVO.getSessionId(), messageVO.getFromId());
        } else {
            messageVO.setFromAvatar(sessionUser.getUserAvatar());
            messageVO.setFromNickname(sessionUser.getUserNickname());
        }
    }

    private void setWaiterData(MessageVO messageVO, Map<String, StoreWaiter> waiterMap) {
        StoreWaiter waiter = waiterMap.get(messageVO.getFromId());
        if (waiter == null) {
            log.warn("客服信息不存在 {} {}", messageVO.getSessionId(), messageVO.getFromId());
        } else {
            messageVO.setFromNickname(waiter.getWaiterNickname());
            messageVO.setFromAvatar(waiter.getWaiterAvatar());
        }
    }

    /**
     * 参数校验
     *
     * @param findSessionMessages
     */
    @Override
    public boolean paramError(FindSessionMessages findSessionMessages) {
        if (findSessionMessages.getSessionId() == null) {
            return true;
        }

        Long topMessageId = findSessionMessages.getTopMessageId();
        Long size = findSessionMessages.getSize();
        if (findSessionMessages.getSessionId() <= 0 || (topMessageId != null && topMessageId <= 0) || (size != null && size <= 0)) {
            return true;
        }
        return false;
    }


}
