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
        // ????????????
        pipeline.addLast(wsServerHeartHandler);
        // ????????????????????????
        pipeline.addLast(wsServerCommonHandler);
        //???????????????Http?????????
        //http?????????
        pipeline.addLast(new HttpServerCodec());
        //http?????????
        pipeline.addLast(new HttpObjectAggregator(imServerProperties.getMaxContentLength()));
        //?????????????????????
        pipeline.addLast(new ChunkedWriteHandler());
        // ????????????
        pipeline.addLast(wsServerHandshakeHandler);
        // WebSocket????????????
        pipeline.addLast(new WebSocketServerCompressionHandler());
        //websocket??????,????????????
        pipeline.addLast(new WebSocketServerProtocolHandler(imServerProperties.getWebSocketPrefix(), true));
//        pipeline.addLast(new ProtobufToWebSocketFrameEncoder());

        // ????????????
        pipeline.addLast(wsServerMessageHandler);
    }


    /**
     * websocket ???protobuf ???bi
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
