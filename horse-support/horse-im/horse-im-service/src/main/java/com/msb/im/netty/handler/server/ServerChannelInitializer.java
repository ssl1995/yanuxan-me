package com.msb.im.netty.handler.server;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.msb.im.netty.ImServerProperties;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: zhou miao
 * @create: 2022/04/15
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Resource
    private WsServerCommonHandler wsServerCommonHandler;
    @Resource
    private WsServerHandshakeHandler wsServerHandshakeHandler;
    @Resource
    private WsServerHeartHandler wsServerHeartHandler;
    @Resource
    private WsServerMessageHandler wsServerMessageHandler;
    @Resource
    private ImServerProperties imServerProperties;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(imServerProperties.getServerReaderIdleTimeSeconds(), 0, 0));
        // 心跳处理
        pipeline.addLast(wsServerHeartHandler);
        // 关闭或者异常处理
        pipeline.addLast(wsServerCommonHandler);
        //以下三个是Http的支持
        //http解码器
        pipeline.addLast(new HttpServerCodec());
        //http聚合器
        pipeline.addLast(new HttpObjectAggregator(imServerProperties.getMaxContentLength()));
        //支持写大数据流
        pipeline.addLast(new ChunkedWriteHandler());
        // 握手处理
        pipeline.addLast(wsServerHandshakeHandler);
        // WebSocket数据压缩
        pipeline.addLast(new WebSocketServerCompressionHandler());
        //websocket支持,设置路由
        pipeline.addLast(new WebSocketServerProtocolHandler(imServerProperties.getWebSocketPrefix(), true));
//        pipeline.addLast(new ProtobufToWebSocketFrameEncoder());

        // 消息处理
        pipeline.addLast(wsServerMessageHandler);
    }


    /**
     * websocket 转protobuf 转bi
     */
    static class ProtobufToWebSocketFrameEncoder extends MessageToMessageEncoder<MessageLiteOrBuilder> {

        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, MessageLiteOrBuilder msg, List<Object> out) throws Exception {

            ByteBuf result = null;

            if (msg instanceof MessageLite) {

                result = Unpooled.wrappedBuffer(((MessageLite) msg).toByteArray());

            }

            if (msg instanceof MessageLite.Builder) {

                result = Unpooled.wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());

            }
            WebSocketFrame frame = new BinaryWebSocketFrame(result);

            out.add(frame);
        }
    }

}
