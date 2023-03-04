package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.module.chat.model.AlreadyReadMessage;
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
 * channel中客服功能用户发送消息已读
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreUserAlreadyReadMessageService extends AbstractClientMessageService<AlreadyReadMessage> {
    @Resource
    private SessionUserService sessionUserService;
    @Resource
    private SessionService sessionService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, AlreadyReadMessage alreadyReadMessage) {
        String userId = getUserId(ctx);
        Integer systemId = getSysId(ctx);
        if (!sessionService.sessionOfUser(alreadyReadMessage.getSessionId(), userId, systemId)) {
            ctx.writeAndFlush( RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "你没有该会话权限", null));
            return false;
        }
        boolean readSuccess = sessionUserService.readMessage(alreadyReadMessage.getSessionId(), userId);
        if (readSuccess) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, null));
        }
        return !readSuccess;
    }

    /**
     * 参数校验
     *
     * @param alreadyReadMessage 已读
     */
    @Override
    public boolean paramError(AlreadyReadMessage alreadyReadMessage) {
        return alreadyReadMessage.getSessionId() == null || alreadyReadMessage.getSessionId() <= 0;
    }


}
