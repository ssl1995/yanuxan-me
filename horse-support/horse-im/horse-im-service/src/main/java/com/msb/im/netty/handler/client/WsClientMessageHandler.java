package com.msb.im.netty.handler.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 处理服务器响应的消息
 *
 * @author zhou miao
 * @date 2022/04/11
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class WsClientMessageHandler extends ChannelInboundHandlerAdapter {
 
    /**
     * 处理消息
     *
     * @param ctx ChannelHandlerContext
     * @param msg Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) msg;
            log.info("收到服务器消息 {} {}", ctx.channel(), textFrame.text());
        } else if (msg instanceof CloseWebSocketFrame) {
            log.info("连接收到关闭帧 {}", ctx.channel());
            ctx.channel().close();
        } else if (msg instanceof PongWebSocketFrame) {
            log.info("收到服务器心跳响应 {} {}", ctx.channel(), msg);
        }
    }

}