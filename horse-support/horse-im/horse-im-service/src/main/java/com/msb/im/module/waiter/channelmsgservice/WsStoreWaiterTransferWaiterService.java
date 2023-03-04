package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.module.waiter.model.channelmessage.StoreWaiterTransferWaiter;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * channel中客服功能客服转移会话
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreWaiterTransferWaiterService extends AbstractClientMessageService<StoreWaiterTransferWaiter> {
    @Resource
    private StoreService storeService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreWaiterTransferWaiter storeWaiterTransferWaiter) {
        String currentWaiter = getStoreWaiterId(ctx);
        storeWaiterTransferWaiter.setStoreId(getStoreId(ctx));
        storeWaiterTransferWaiter.setCurrentWaiterId(currentWaiter);
        storeWaiterTransferWaiter.setTraceId(traceId);
        storeWaiterTransferWaiter.setTraceType(traceType);
        storeWaiterTransferWaiter.setSysId(getStoreSysId(ctx));
//        storeService.transferSession(storeWaiterTransferWaiter);
        return false;
    }

    /**
     * 参数校验
     *
     * @param storeWaiterTransferWaiter
     */
    @Override
    public boolean paramError(StoreWaiterTransferWaiter storeWaiterTransferWaiter) {
        return storeWaiterTransferWaiter.getToWaiterId() == null
                || storeWaiterTransferWaiter.getSessionId() == null
                ;
    }


}
