package com.msb.im.netty.server;

import com.msb.im.netty.ImProperties;
import com.msb.im.netty.ImServerProperties;
import com.msb.im.netty.handler.server.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;


/**
 * @description:
 * @author: zhou miao
 * @create: 2022/04/08
 */
@Slf4j
public class WebSocketServer extends ServerAsClientAble implements IServer {

    private static volatile boolean isStart;
    private int port;
    private String ip;
    private NioEventLoopGroup boos;
    private NioEventLoopGroup worker;
    private Channel serverChannel;
    private ServerChannelInitializer serverChannelInitializer;
    // 服务器线程池 主要用来执行作为客户端连接其他服务器
    private ExecutorService taskExecutorService;
    private ImProperties imServerProperties;
    private final Object lock = new Object();

    public WebSocketServer() {
    }

    public WebSocketServer(ServerChannelInitializer serverChannelInitializer, ImServerProperties properties, ExecutorService taskExecutorService) throws UnknownHostException {
        this.serverChannelInitializer = serverChannelInitializer;
        this.imServerProperties = properties;
        this.ip = InetAddress.getLocalHost().getHostAddress();
        this.port = properties.getPort();
        this.boos = new NioEventLoopGroup(properties.getBossNthreads());
        this.worker = new NioEventLoopGroup(properties.getWorkerNthreads());
        this.taskExecutorService = taskExecutorService;
    }

    // 服务器配置
    @Override
    public void setProperties(ImProperties imProperties) {
        this.imServerProperties = imProperties;
    }

    @Override
    public ImProperties getProperties() {
        return imServerProperties;
    }

    /**
     * 获取server的端口号
     *
     * @return
     */
    @Override
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取server的ip
     *
     * @return
     */
    @Override
    public String getIp() {
        return ip;
    }


    @Override
    public void shutdown() {
        if (boos != null) {
            boos.shutdownGracefully();
        }

        if (worker != null) {
            worker.shutdownGracefully();
        }

        if (taskExecutorService != null) {
            taskExecutorService.shutdown();
        }
    }

    /**
     * 启动netty服务
     */
    @Override
    public void start(Runnable startedCallback) {
        synchronized (lock) {
            try {
                if (isStart()) {
                    log.error("重复启动 {}", this);
                    return;
                }
                ServerBootstrap b = new ServerBootstrap();
                b.group(boos, worker)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(serverChannelInitializer)
                        .option(ChannelOption.SO_BACKLOG, imServerProperties.getSoBackLog())
                        .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
                ChannelFuture channelFuture = b.bind(port).sync();
                serverChannel = channelFuture.channel();
                this.ip = InetAddress.getLocalHost().getHostAddress();
                log.info("服务器 {}:{} 启动", getIp(), getPort());
                isStart = true;
                if (startedCallback != null) {
                    startedCallback.run();
                }
                serverChannel.closeFuture().addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        shutdown();
                        log.error("服务器关闭...");
                        System.exit(1);
                    }
                });
            } catch (Exception e) {
                shutdown();
            }
        }
    }

    @Override
    public boolean isStart() {
        return isStart;
    }

    @Override
    public String toString() {
        return "WebSocketServer{" +
                "port=" + port +
                ", ip='" + ip + '\'' +
                '}';
    }

    @Override
    public ExecutorService getTaskExecutorService() {
        return taskExecutorService;
    }
}