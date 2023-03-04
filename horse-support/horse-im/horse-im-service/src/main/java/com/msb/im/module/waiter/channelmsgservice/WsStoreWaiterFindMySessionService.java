package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.model.vo.ListSessionVO;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterFindMySession;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * channel中客服功能客服查询会话
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreWaiterFindMySessionService extends AbstractClientMessageService<StoreWaiterFindMySession> {
    @Resource
    private StoreService storeService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreWaiterFindMySession storeWaiterFindMySession) {
        String waiterId = getStoreWaiterId(ctx);
        Integer systemId = getStoreSysId(ctx);
        Long storeId = getStoreId(ctx);

        ListSessionVO listSessionVO = storeService.findWaiterSessions(systemId, storeId, waiterId);

        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, listSessionVO));
        return false;
    }

    /**
     * 参数校验
     *
     * @param storeWaiterFindMySession
     */
    @Override
    public boolean paramError(StoreWaiterFindMySession storeWaiterFindMySession) {
        return false;
    }


}
