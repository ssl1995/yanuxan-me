package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.module.waiter.model.channelmessage.StoreWaiterPingPong;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.portobuf.ReqMessageProto;
import com.msb.im.util.AddressUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * channel中客服功能客服心跳
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreWaiterPingPongService extends AbstractClientMessageService<StoreWaiterPingPong> {
    @Resource
    private StoreService storeService;

    @Override
    public boolean handleMessageBeforeError(ChannelHandlerContext ctx,  ReqMessageProto.Model reqMessage) {
        return false;
    }

    @Override
    public void handleMessageAfter(ChannelHandlerContext ctx,  ReqMessageProto.Model reqMessage) {
    }

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreWaiterPingPong storeWaiterPingPong) {
        Integer sysId = getStoreSysId(ctx);
        String waiterId = getStoreWaiterId(ctx);
        Long storeId = getStoreId(ctx);
        String localAddress = AddressUtil.localAddress(ctx.channel());
        storeService.renewalWaiterStoreSessionHeart(sysId, storeId, waiterId, localAddress);
        return false;
    }

    /**
     * 参数校验
     *
     * @param storeWaiterPingPong
     */
    @Override
    public boolean paramError(StoreWaiterPingPong storeWaiterPingPong) {
        return false;
    }


}
