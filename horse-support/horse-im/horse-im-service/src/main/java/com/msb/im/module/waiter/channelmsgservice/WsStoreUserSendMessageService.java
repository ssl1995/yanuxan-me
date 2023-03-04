package com.msb.im.module.waiter.channelmsgservice;

import com.msb.im.api.enums.MessageTypeEnum;
import com.msb.im.module.waiter.model.channelmessage.StoreUserSendMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.service.StoreConfigService;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.service.SessionService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * channel中客服功能用户发送消息
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Service
@Slf4j
public class WsStoreUserSendMessageService extends AbstractClientMessageService<StoreUserSendMessage> {
    @Resource
    private StoreService storeService;
    @Resource
    private StoreConfigService storeConfigService;
    @Resource
    private SessionService sessionService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, StoreUserSendMessage storeUserSendMessage) {
        Integer sysId = getSysId(ctx);
        StoreConfig storeConfig = storeConfigService.findBySysId(sysId);
        if (storeConfig == null) {
            log.warn("无权限调用 {}", sysId);
            return true;
        }
        String userId = getUserId(ctx);
        if (!sessionService.sessionOfUser(storeUserSendMessage.getToSessionId(), userId, sysId)) {
            log.warn("无会话权限 {}", sysId);
            return true;
        }

        storeUserSendMessage.setFromId(userId);
        storeUserSendMessage.setSysId(sysId);
        storeUserSendMessage.setStoreId(storeConfig.getId());
        storeUserSendMessage.setTraceId(traceId);
        storeUserSendMessage.setTraceType(traceType);

        storeService.userSendMessage(storeUserSendMessage);
        return false;
    }

    /**
     * 参数校验
     *
     * @param storeUserSendMessage
     */
    @Override
    public boolean paramError(StoreUserSendMessage storeUserSendMessage) {
        if (storeUserSendMessage.getToSessionId() == null) {
            return true;
        }
        if (!MessageTypeEnum.ofCode(storeUserSendMessage.getType())) {
            return true;
        }
        if (storeUserSendMessage.getPayload() == null) {
            return true;
        }
        return false;
    }


}
