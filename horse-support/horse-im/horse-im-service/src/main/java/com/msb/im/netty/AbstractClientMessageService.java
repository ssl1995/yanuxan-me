package com.msb.im.netty;

import com.alibaba.fastjson.JSON;
import com.msb.im.module.ChannelManager;
import com.msb.im.util.RspFrameUtil;
import com.msb.im.module.chat.channel.UserChannelManager;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.module.waiter.channel.StoreWaiterChannelManager;
import com.msb.im.module.waiter.service.StoreConfigService;
import com.msb.im.portobuf.ReqMessageProto;
import com.msb.im.redis.RedisService;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;

/**
 * 处理客户端消息的基类，业务处理是通过实现它的抽象方法
 *
 * @param <T>
 */
@Service
@Slf4j
public abstract class AbstractClientMessageService<T> {
    @Resource
    protected RedisService redisService;
    @Resource
    protected ChannelManager channelManager;
    @Resource
    protected UserChannelManager userChannelManager;
    @Resource
    protected StoreWaiterChannelManager storeWaiterChannelManager;
    @Resource
    protected StoreConfigService storeConfigService;

    protected boolean userSysExistStore(ChannelHandlerContext ctx) {
        return storeConfigService.sysExist(getSysId(ctx));
    }

    protected String getUserId(ChannelHandlerContext ctx) {
        return userChannelManager.getUserId(ctx.channel());
    }

    protected Integer getSysId(ChannelHandlerContext ctx) {
        return userChannelManager.getSystemId(ctx.channel());
    }

    protected Long getStoreId(ChannelHandlerContext ctx) {
        return storeWaiterChannelManager.getStoreId(ctx.channel());
    }

    protected Integer getStoreSysId(ChannelHandlerContext ctx) {
        return storeWaiterChannelManager.getSystemId(ctx.channel());
    }

    protected String getStoreWaiterId(ChannelHandlerContext ctx) {
        return storeWaiterChannelManager.getWaiterId(ctx.channel());
    }

    public boolean handleMessageBeforeError(ChannelHandlerContext ctx, ReqMessageProto.Model reqMessage) {
        if (redisService.existTraceId(reqMessage.getTraceId())) { // 重复消息
            log.warn("重复消息 {}", reqMessage);
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(reqMessage.getTraceId(), reqMessage.getTraceType(), RspMessage.ERROR, "重复发送请求", null));
            return true;
        }
        return false;
    }

    public void handleMessage(ChannelHandlerContext ctx, ReqMessageProto.Model reqMessage) {
        try {
            if (handleMessageBeforeError(ctx, reqMessage)) {
                return;
            }
            ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            T t = (T) JSON.parseObject(reqMessage.getContent(), type);
            if (paramError(t)) {
                ctx.writeAndFlush(RspFrameUtil.createRspFrame(reqMessage.getTraceId(), reqMessage.getTraceType(), RspMessage.ERROR, "参数错误", t));
                return;
            }
            if (handleError(ctx, reqMessage.getTraceId(), reqMessage.getTraceType(), t)) {
                return;
            }
            handleMessageAfter(ctx, reqMessage);
        } catch (Exception e) {
            log.error("处理客户端消息发生错误 {} {}", e.getStackTrace(), e.getMessage());
            ctx.writeAndFlush(RspFrameUtil.createRspFrame(reqMessage.getTraceId(), reqMessage.getTraceType(), RspMessage.ERROR, e.getMessage(), null));
        }
    }

    /**
     * 业务方法
     *
     * @param ctx
     * @param traceId
     * @param traceType
     * @param t
     */
    protected abstract boolean handleError(ChannelHandlerContext ctx, String traceId, Integer traceType, T t);

    /**
     * 参数校验
     *
     * @param t
     * @return
     */
    protected abstract boolean paramError(T t);

    public void handleMessageAfter(ChannelHandlerContext ctx, ReqMessageProto.Model reqMessage) {
        redisService.addTraceId(reqMessage.getTraceId());
    }
}
