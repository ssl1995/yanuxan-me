package com.msb.im.module.chat.channelmsgservice;

import com.msb.im.convert.TextWebSocketFrameConvert;
import com.msb.im.module.chat.model.DeleteSessionMessage;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.service.SessionService;
import com.msb.im.service.SessionUserService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 心跳消息处理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Service
@Slf4j
public class WsClientDeleteSessionService extends AbstractClientMessageService<DeleteSessionMessage> {
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private SessionService sessionService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, DeleteSessionMessage deleteSessionMessage) {
        Long sessionId = deleteSessionMessage.getSessionId();
        String userId = getUserId(ctx);
        Integer systemId = getSysId(ctx);
        if (!sessionService.sessionOfUser(sessionId, userId, systemId)) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "无权操作该会话", null));
            return true;
        }
        sessionUserService.deleteSession(userId, sessionId, systemId);
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, null));
        return false;
    }

    /**
     * 参数校验
     *
     * @param deleteSessionMessage
     */
    @Override
    public boolean paramError(DeleteSessionMessage deleteSessionMessage) {
        return deleteSessionMessage.getSessionId() == null || deleteSessionMessage.getSessionId() <= 0;
    }


}
