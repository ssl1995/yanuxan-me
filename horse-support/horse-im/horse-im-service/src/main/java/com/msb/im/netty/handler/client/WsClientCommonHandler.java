package com.msb.im.netty.handler.client;

import com.msb.im.netty.client.WebSocketClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: ws 连接通用处理器 处理通道的断开
 * @author: zhou miao
 * @create: 2022/04/09
 */
@Slf4j
public class WsClientCommonHandler extends ChannelInboundHandlerAdapter {

    private final WebSocketClient client;

    public WsClientCommonHandler(WebSocketClient client) {
        this.client = client;
    }

    /**
     * 关闭连接
     *
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.warn("客户端断开连接 移除连接的服务器 关闭通道{}", ctx.channel());
        client.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("客户端连接发生错误 {} {}", ctx.channel(), cause.getMessage());
    }

}
