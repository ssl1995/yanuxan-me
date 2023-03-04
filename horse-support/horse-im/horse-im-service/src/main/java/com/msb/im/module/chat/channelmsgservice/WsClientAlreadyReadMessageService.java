package com.msb.im.module.chat.channelmsgservice;

import com.msb.im.module.chat.model.AlreadyReadMessage;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterAlreadyReadMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterSendMessage;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 消息已读处理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Service
@Slf4j
public class WsClientAlreadyReadMessageService extends AbstractClientMessageService<AlreadyReadMessage> {
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private SessionService sessionService;
    @Resource
    private StoreService storeService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, AlreadyReadMessage alreadyReadMessage) {
        boolean waiterConnect = channelManager.isWaiterConnect(ctx.channel());
        if (waiterConnect) {
            Long storeId = getStoreId(ctx);
            String waiterId = getStoreWaiterId(ctx);
            Integer storeSysId = getStoreSysId(ctx);
            waiterReadMessage(traceId, traceType, storeSysId, storeId, waiterId, alreadyReadMessage.getSessionId());
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, null));
        } else {
            String userId = getUserId(ctx);
            Integer systemId = getSysId(ctx);
            if (!sessionService.sessionOfUser(alreadyReadMessage.getSessionId(), userId, systemId)) {
                ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "你没有该会话权限", null));
                return false;
            }
            boolean readSuccess = sessionUserService.readMessage(alreadyReadMessage.getSessionId(), userId);
            if (readSuccess) {
                ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, null));
            }
        }
        return false;
    }

    public void waiterReadMessage(String traceId, Integer traceType, Integer sysId, Long storeId, String waiterId, Long sessionId) {
        if (!sessionService.sessionOfUser(sessionId, storeId.toString(), sysId)) {
            log.error("无操作权限 {} {}", storeId, sessionId);
            return;
        }

        StoreWaiterAlreadyReadMessage storeWaiterAlreadyReadMessage = new StoreWaiterAlreadyReadMessage();
        storeWaiterAlreadyReadMessage.setWaiterId(waiterId);
        storeWaiterAlreadyReadMessage.setStoreId(storeId);
        storeWaiterAlreadyReadMessage.setSessionId(sessionId);
        storeWaiterAlreadyReadMessage.setTraceId(traceId);
        storeWaiterAlreadyReadMessage.setTraceType(traceType);
        storeService.waiterReadMessage(storeWaiterAlreadyReadMessage);
    }

    /**
     * 参数校验
     *
     * @param alreadyReadMessage
     */
    @Override
    public boolean paramError(AlreadyReadMessage alreadyReadMessage) {
        return alreadyReadMessage.getSessionId() == null;
    }


}
