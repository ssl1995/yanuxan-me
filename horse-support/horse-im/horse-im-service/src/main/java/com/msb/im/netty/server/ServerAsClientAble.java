package com.msb.im.netty.server;

import com.msb.im.netty.ConnectTypeEnum;
import com.msb.im.netty.ImProperties;
import com.msb.im.netty.client.IClient;
import com.msb.im.netty.client.WebSocketClient;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.msb.im.netty.ImServerProperties.ADDRESS_SPLIT;
import static com.msb.im.netty.ImServerProperties.WS_PROTOCOL;


/**
 * @description: 服务器作为别的服务客户端的能力
 * @author: zhou miao
 * @create: 2022/04/11
 */
@Slf4j
public abstract class ServerAsClientAble implements IThreadPool {
    private static final Object lock = new Object();

    private final Map<String, IClient> asClientMap = new ConcurrentHashMap<>();

    public Map<String, IClient> getAsClientMap() {
        return asClientMap;
    }

    /**
     * 添加server作为其他server的client
     *
     * @param client
     */
    public void addAsClient(IClient client) {
        getAsClientMap().put(createClientKey(client.getServerIp(), client.getServerPort()), client);
    }

    /**
     * 获取服务器保存的客户端
     */
    public IClient getAsClient(String address) { // address = ip:port
        return getAsClientMap().get(address);
    }

    /**
     * 其他netty服务下线时 移除当前server作为其他服务的client
     *
     * @param ip
     * @param port
     */
    public void removeAsClient(String ip, int port) {
        if (ip == null) {
            return;
        }
        IClient remove = getAsClientMap().remove(createClientKey(ip, port));
        if (remove != null) {
            remove.close();
        }
    }

    /**
     * 是否已经包含对应服务器的client
     *
     * @param ip
     * @param port
     */
    public boolean containAsClient(String ip, int port) {
        return asClientMap.containsKey(createClientKey(ip, port));
    }

    /**
     * * 是否已经包含对应服务器的client
     *
     * @param address
     */
    public boolean containAsClient(String address) {
        return asClientMap.containsKey(address);
    }

    /**
     * 创建clientKey
     *
     * @param ip
     * @param port
     * @return
     */
    private String createClientKey(String ip, int port) {
        return ip + ADDRESS_SPLIT + port;
    }

    /**
     * 连接单台其他服务器
     *
     * @param currentServer
     * @param connectServerIp
     * @param connectServerPort
     */
    public void connectServer(IServer currentServer, String connectServerIp, int connectServerPort) {
        synchronized (lock) {
            if (!containAsClient(connectServerIp, connectServerPort)) {
                getTaskExecutorService().submit(() -> {
                    ImProperties properties = currentServer.getProperties();
                    String wsUri = WS_PROTOCOL + connectServerIp + ADDRESS_SPLIT + connectServerPort + properties.getWebSocketPrefix() + "?type=" + ConnectTypeEnum.INTERNAL.getCode();
                    WebSocketClient webSocketClient = new WebSocketClient(connectServerIp, connectServerPort, wsUri, this, null);
                    Runnable openSuccessCallback = () -> {
                        addAsClient(webSocketClient);
                    };
                    log.info("{} 作为客户端连接其他服务器 {} {}", currentServer, connectServerIp, connectServerPort);
                    webSocketClient.open(openSuccessCallback);
                });
            }
        }
    }


}
