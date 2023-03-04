package com.msb.im.module.chat.channelmsgservice;

import com.msb.im.module.chat.model.PingMessage;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.netty.service.UserConnectService;
import com.msb.im.portobuf.ReqMessageProto;
import com.msb.im.redis.RedisService;
import com.msb.im.util.AddressUtil;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.msb.im.module.chat.model.PongMessage.DEFAULT_PONG_MESSAGE;
import static com.msb.im.module.chat.model.ReqMessage.DEFAULT_PING_REQ_MESSAGE;
import static com.msb.im.module.chat.model.RspMessage.SUCCESS;

/**
 * 心跳消息处理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Service
@Slf4j
public class WsClientPingPongMessageService extends AbstractClientMessageService<PingMessage> {
    @Resource
    private RedisService redisService;
    @Resource
    private UserConnectService userConnectService;
    @Resource
    private StoreService storeService;

    @Override
    public boolean handleMessageBeforeError(ChannelHandlerContext ctx, ReqMessageProto.Model reqMessage) {
        return false;
    }

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, PingMessage pingMessage) {
        if (!DEFAULT_PING_REQ_MESSAGE.getContent().equals(pingMessage)) {
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "心跳消息体错误", null));
            return true;
        }
        boolean waiterConnect = channelManager.isWaiterConnect(ctx.channel());
        if (waiterConnect) {
            // 客服心跳
            Integer sysId = getStoreSysId(ctx);
            String waiterId = getStoreWaiterId(ctx);
            Long storeId = getStoreId(ctx);
            String localAddress = AddressUtil.localAddress(ctx.channel());
            storeService.renewalWaiterStoreSessionHeart(sysId, storeId, waiterId, localAddress);
        } else {
            // 用户心跳
            log.debug("收到客户端{}心跳消息{}", ctx, pingMessage);

            // 续期
            String userId = getUserId(ctx);
            Integer systemId = getSysId(ctx);
            String address = AddressUtil.localAddress(ctx.channel());
            redisService.renewalUserServerAddress(userId, address, systemId);
        }
        ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, SUCCESS, null, DEFAULT_PONG_MESSAGE));
        return false;
    }


    @Override
    protected boolean paramError(PingMessage pingMessage) {
        return false;
    }

    @Override
    public void handleMessageAfter(ChannelHandlerContext ctx, ReqMessageProto.Model reqMessage) {
    }
}
