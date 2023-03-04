package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.model.vo.MessageVO;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterFindSessionMessages;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.service.SessionService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * channel中客服功能客服查询会话消息列表
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreWaiterFindSessionMessagesService extends AbstractClientMessageService<StoreWaiterFindSessionMessages> {
    @Resource
    private StoreService storeService;
    @Resource
    private SessionService sessionService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreWaiterFindSessionMessages storeWaiterFindSessionMessages) {
        storeWaiterFindSessionMessages.setTraceId(traceId);
        storeWaiterFindSessionMessages.setTraceType(traceType);

        Long sessionId = storeWaiterFindSessionMessages.getSessionId();

        Long storeId = getStoreId(ctx);
        int systemId = getStoreSysId(ctx);
        if (!sessionService.sessionOfUser(sessionId, storeId.toString(), systemId)) {
            log.error("无权操作会话 {}", storeWaiterFindSessionMessages);
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "无操作权限", Collections.emptyList()));
            return true;
        }

        List<MessageVO> messageVOS = storeService.findWaiterSessionMessages(systemId, storeWaiterFindSessionMessages.getSessionId(), storeId.toString(), storeWaiterFindSessionMessages.getTopMessageId(), storeWaiterFindSessionMessages.getSize());

        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, messageVOS));
        return false;
    }

    /**
     * 参数校验
     *
     * @param storeWaiterFindSessionMessages
     */
    @Override
    public boolean paramError(StoreWaiterFindSessionMessages storeWaiterFindSessionMessages) {
        return storeWaiterFindSessionMessages.getSessionId() == null
                || (storeWaiterFindSessionMessages.getTopMessageId() != null && storeWaiterFindSessionMessages.getTopMessageId() <= 0)
                || (storeWaiterFindSessionMessages.getSize() != null && storeWaiterFindSessionMessages.getSize() <= 0);
    }


}
