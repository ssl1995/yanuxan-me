package com.msb.im.module.chat.channelmsgservice;

import com.msb.im.api.enums.SessionTypeEnum;
import com.msb.im.api.enums.TicketTypeEnum;
import com.msb.im.convert.SessionConvert;
import com.msb.im.model.entity.Session;
import com.msb.im.model.entity.ThirdSystemConfig;
import com.msb.im.model.vo.SessionVO;
import com.msb.im.module.chat.model.CreateSessionMessage;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.AbstractClientMessageService;
import com.msb.im.netty.model.HandshakeParam;
import com.msb.im.service.SessionService;
import com.msb.im.service.ThirdSystemConfigService;
import com.msb.im.util.RspFrameUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 心跳消息处理类
 *
 * @author zhou miao
 * @date 2022/05/12
 */
@Service
@Slf4j
public class WsClientCreateSessionService extends AbstractClientMessageService<CreateSessionMessage> {
    @Resource
    private SessionService sessionService;
    @Resource
    private SessionConvert sessionConvert;
    @Resource
    private ThirdSystemConfigService thirdSystemConfigService;
    @Resource
    private StoreService storeService;

    @Override
    protected boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, CreateSessionMessage createSessionMessage) {
        Integer sysId = getSysId(ctx);
        String userId = getUserId(ctx);
        if (Objects.equals(createSessionMessage.getSessionType(), SessionTypeEnum.STORE.getCode())) {
            // 客服会话
            StoreConfig storeConfig = storeConfigService.findBySysId(sysId);
            if (storeConfig == null) {
                log.warn("无权限调用 {}", sysId);
                ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "无权限调用", null));
                return false;
            }
            Session storeSession = storeService.createStoreSession(userId, sysId, storeConfig.getId(), channelManager.get(ctx.channel()));
            SessionVO sessionVO = sessionConvert.toVo(storeSession);
            sessionVO.setFromNickname(storeConfig.getName());
            sessionVO.setFromAvatar(storeConfig.getAvatar());
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, sessionVO));
        } else if (Objects.equals(createSessionMessage.getSessionType(), SessionTypeEnum.SINGLE.getCode())) {
            // 单人会话
            ThirdSystemConfig thirdSystemConfig = thirdSystemConfigService.checkTicketBySysIdAndReturnEntity(sysId, createSessionMessage.getTicket(), userId, TicketTypeEnum.CREATE_SINGLE_SESSION_TICKET);
            if (thirdSystemConfig == null) {
                log.warn("无权限调用 {}", sysId);
                ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.ERROR, "无权限调用", null));
                return false;
            }
            createSessionMessage.setSysId(sysId);
            createSessionMessage.setFromId(userId);
            setUserData(createSessionMessage, ctx.channel());
            Session singleSession = sessionService.createSingleSession(createSessionMessage);
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(traceId, traceType, RspMessage.SUCCESS, null, sessionConvert.toVo(singleSession)));
        }
        return false;
    }

    private void setUserData(CreateSessionMessage createSessionMessage, Channel channel) {
        HandshakeParam handshakeParam = channelManager.get(channel);
        createSessionMessage.setFromAvatar(handshakeParam.getAvatar());
        createSessionMessage.setFromNickname(handshakeParam.getNickname());
    }

    /**
     * 参数校验
     *
     * @param createSessionMessage 参数
     */
    @Override
    public boolean paramError(CreateSessionMessage createSessionMessage) {
        Integer sessionType = createSessionMessage.getSessionType();
        if (Objects.equals(sessionType, SessionTypeEnum.STORE.getCode())) {
            return false;
        } else if (Objects.equals(sessionType, SessionTypeEnum.SINGLE.getCode())){
            return createSessionMessage.getToId() == null
                    || createSessionMessage.getToAvatar() == null
                    || createSessionMessage.getToNickname() == null
                    || createSessionMessage.getTicket() == null
                    ;
        } else {
            return false;
        }

    }

}
