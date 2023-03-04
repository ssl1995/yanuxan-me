package com.msb.im.netty.client;

import com.msb.im.context.ImServerContext;
import com.msb.im.netty.handler.client.WsClientCommonHandler;
import com.msb.im.netty.handler.client.WsClientHandshakeHandler;
import com.msb.im.netty.handler.client.WsClientHeartHandler;
import com.msb.im.netty.handler.client.WsClientMessageHandler;
import com.msb.im.netty.server.IServer;
import com.msb.im.netty.server.ServerAsClientAble;
import com.msb.im.portobuf.RspMessageProto;
import com.msb.im.redis.RedisService;
import com.msb.im.util.AddressUtil;
import com.msb.im.util.RspFrameUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * websocket客户端
 *
 * @author zhou miao
 * @date 2022/04/08
 */
@Slf4j
public class WebSocketClient extends OfServerAble implements IClient {
    private static final WsClientMessageHandler WS_CLIENT_MESSAGE_HANDLER = new WsClientMessageHandler();

    private final EventLoopGroup group = new NioEventLoopGroup(2);
    private Channel channel;
    private final int serverPort;
    private final String serverIp;
    private final String wsUri;
    private volatile boolean serverIsClosed = false;
    private volatile boolean serverIsClosing = false;
    private final Runnable closeCallback;

    public WebSocketClient(String serverIp, int serverPort, String wsUri, ServerAsClientAble server, Runnable closeCallback) {
        super(server);
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.wsUri = wsUri;
        this.closeCallback = closeCallback;
    }


    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public String getServerIp() {
        return serverIp;
    }

    /**
     * 打开连接netty服务器
     */
    @Override
    public boolean open(Runnable openSuccessCallback) {
        try {
            ServerAsClientAble ofServer = getOfServer();
            IServer server = (IServer) ofServer;
            Bootstrap boot = new Bootstrap();
            WebSocketClient current = this;
            WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(URI.create(wsUri), WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
            WsClientHandshakeHandler wsClientHandshakeHandler = new WsClientHandshakeHandler(webSocketClientHandshaker);
            channel = boot.option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel socketChannel) {
                            socketChannel
                                    .pipeline()
                                    .addLast(new IdleStateHandler(0, server.getProperties().getClientReaderIdleTimeSeconds(), 0))
                                    .addLast(new WsClientHeartHandler(current))
                                    .addLast(new WsClientCommonHandler(current))
                                    .addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(server.getProperties().getMaxContentLength()))
                                    .addLast(wsClientHandshakeHandler)
                                    .addLast(WS_CLIENT_MESSAGE_HANDLER)
                            ;
                        }
                    }).connect(new InetSocketAddress(serverIp, serverPort))
                    .sync()
                    .channel();
            wsClientHandshakeHandler.handshakeFuture().sync();
            log.info("握手完成 客户端 {}:{} 连接上了服务器 {}:{} ", AddressUtil.localIp(channel), AddressUtil.localPort(channel), AddressUtil.remoteIp(channel), AddressUtil.remotePort(channel));

            if (openSuccessCallback != null) {
                openSuccessCallback.run();
            }

            channel.closeFuture().addListener((ChannelFutureListener) channelFuture -> close());

            while (!serverIsClosed) {
                channel.writeAndFlush(new PingWebSocketFrame());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                }
            }
        } catch (Exception e) {
            // 连接失败 释放当前正在连接的服务器
            log.error("连接服务器失败 {}", e.getMessage());
        } finally {
            close();
        }
        return false;
    }

    /**
     * 关闭客户端的连接
     */
    @Override
    public void close() {
        if (serverIsClosing || serverIsClosed) {
            return;
        }
        serverIsClosing = true;

        log.info("客户端关闭连接 channel={}", channel);
        if (closeCallback != null) {
            closeCallback.run();
        }

        removeConnectServer();
        group.shutdownGracefully();
        serverIsClosed = true;
        serverIsClosing = false;
    }

    /**
     * 服务器断连 删除客户端对应的服务器 删除当前服务器保存的客户端
     */
    public void removeConnectServer() {
        ServerAsClientAble ofServer = getOfServer();
        ofServer.removeAsClient(serverIp, serverPort);
        RedisService redisService = ImServerContext.getRedisService();
        if (redisService == null) {
            log.warn("redisService为空");
            return;
        }
        String address = AddressUtil.address(serverIp, serverPort);
        log.warn("从redis移除连接的服务器 {}", address);
        redisService.removeNettyServer(address);
    }

    @Override
    public void sendMessage(RspMessageProto.Model model) {
        if (!channel.isActive()) {
            close();
            return;
        }
        channel.writeAndFlush(RspFrameUtil.createRspFrame(model));
    }

    @Override
    public String toString() {
        return "WebSocketClient{" +
                "port=" + serverPort +
                ", ip='" + serverIp + '\'' +
                ", channel=" + channel +
                ", wsUri='" + wsUri + '\'' +
                '}';
    }
}
