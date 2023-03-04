package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterAlreadyReadMessage;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.service.SessionService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * channel中客服功能客服消息已读
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreWaiterAlreadyReadMessageService extends AbstractClientMessageService<StoreWaiterAlreadyReadMessage> {
    @Resource
    private StoreService storeService;
    @Resource
    private SessionService sessionService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreWaiterAlreadyReadMessage storeWaiterAlreadyReadMessage) {
        Long storeId = getStoreId(ctx);
        String waiterId = getStoreWaiterId(ctx);
        if (!sessionService.sessionOfUser(storeWaiterAlreadyReadMessage.getSessionId(), storeId.toString(), getStoreSysId(ctx))) {
            log.error("无操作权限 {} {}", storeId, storeWaiterAlreadyReadMessage);
            return false;
        }

        storeWaiterAlreadyReadMessage.setWaiterId(waiterId);
        storeWaiterAlreadyReadMessage.setStoreId(storeId);
        storeWaiterAlreadyReadMessage.setTraceId(traceId);
        storeWaiterAlreadyReadMessage.setTraceType(traceType);
        storeService.waiterReadMessage(storeWaiterAlreadyReadMessage);
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, null));
        return false;
    }

    /**
     * 参数校验
     *
     * @param storeUserSendMessage
     */
    @Override
    public boolean paramError(StoreWaiterAlreadyReadMessage storeUserSendMessage) {
        return storeUserSendMessage.getSessionId() == null
                || storeUserSendMessage.getSessionId() <= 0
                ;
    }


}
