package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.module.waiter.model.channelmessage.StoreWaiterSendMessage;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * channel中客服功能客服发送消息
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreWaiterSendMessageService extends AbstractClientMessageService<StoreWaiterSendMessage> {
    @Resource
    private StoreService storeService;


    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreWaiterSendMessage storeWaiterSendMessage) {
        storeWaiterSendMessage.setFromId(getStoreWaiterId(ctx));
        storeWaiterSendMessage.setStoreId(getStoreId(ctx));
        storeWaiterSendMessage.setTraceId(traceId);
        storeWaiterSendMessage.setTraceType(traceType);
        storeWaiterSendMessage.setSysId(getStoreSysId(ctx));

        storeService.waiterSendMessage(storeWaiterSendMessage);
        return false;
    }

    /**
     * 参数校验
     *
     * @param storeWaiterSendMessage
     */
    @Override
    public boolean paramError(StoreWaiterSendMessage storeWaiterSendMessage) {
        if (storeWaiterSendMessage.getToSessionId() == null) {
            return true;
        }
        if (!MessageTypeEnum.ofCode(storeWaiterSendMessage.getType())) {
            return true;
        }
        if (storeWaiterSendMessage.getPayload() == null) {
            return true;
        }
        return false;
    }


}
