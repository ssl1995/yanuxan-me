package com.msb.im.netty.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.msb.im.netty.handler.client.WsClientHandshakeHandler;
import com.msb.im.portobuf.ReqMessageProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class ClientStarter {

    public static void main(String[] args) throws UnknownHostException, InterruptedException, InvalidProtocolBufferException {
        Bootstrap boot = new Bootstrap();
        WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(URI.create("ws://localhost:9090/ws"), WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        WsClientHandshakeHandler wsClientHandshakeHandler = new WsClientHandshakeHandler(webSocketClientHandshaker);
        Channel channel = boot.option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(65536))
                                .addLast(wsClientHandshakeHandler)
                                ;
                    }
                })
                .connect("localhost", 9090)
                .sync().channel();
        while (true) {
//            ReqMessageProto.Model.Builder builder = ReqMessageProto.Model.newBuilder();
//            builder.setContent("3");
//            builder.setTraceType("2");
//            builder.setTraceId("1");
//            byte[] result = builder.build().toByteArray();
//            ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer().writeBytes(builder.build().toByteArray());
//            ReqMessageProto.Model model = ReqMessageProto.Model.parseFrom(result);
//            System.out.println(model);
//            BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(byteBuf);
//            channel.writeAndFlush(binaryWebSocketFrame);
//            TimeUnit.SECONDS.sleep(2);
        }

    }

}
