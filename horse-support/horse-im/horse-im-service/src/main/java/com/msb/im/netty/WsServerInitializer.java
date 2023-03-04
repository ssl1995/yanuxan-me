package com.msb.im.netty;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.msb.im.module.waiter.service.StoreService;
import com.msb.im.netty.handler.server.ServerChannelInitializer;
import com.msb.im.netty.server.IServer;
import com.msb.im.netty.server.ServerAsClientAble;
import com.msb.im.netty.server.WebSocketServer;
import com.msb.im.nacos.ImNacosService;
import com.msb.im.redis.RedisService;
import com.msb.im.redis.SystemRedisService;
import com.msb.im.service.IIdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: websocket服务器
 * @author: zhou miao
 * @create: 2022/04/14
 */
@Configuration
@Slf4j
@EnableConfigurationProperties({ImServerProperties.class})
public class WsServerInitializer implements CommandLineRunner {
    @Autowired
    private ServerChannelInitializer serverChannelInitializer;
    @Autowired
    private ImNacosService nacosService;
    @Autowired
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    @Autowired
    private ImServerProperties imServerProperties;
    @Autowired
    private RedisService redisService;
    @Resource
    private IIdService iIdService;
    @Resource
    private SystemRedisService systemRedisService;
    @Resource
    private StoreService storeService;


    @Bean
    public IServer webSocketServer() throws UnknownHostException {
        return new WebSocketServer(serverChannelInitializer, imServerProperties, scheduledThreadPoolExecutor);
    }

    @Override
    public void run(String... args) throws Exception {
        initWebSocketServer();
        initCache();
    }

    private void initCache() {
        iIdService.loadDbMaxMessageId2Cache();
        iIdService.loadDbMaxSessionId2Cache();
        iIdService.loadDbMaxSessionUserId2Cache();
        systemRedisService.loadThirdSystemConfig2Cache();
    }

    private void initWebSocketServer() throws UnknownHostException {
        IServer webSocketServer = webSocketServer();

        Runnable startedCallback = () -> {
            try {

                List<WebSocketServer> servers = redisService.allNettyServer();
                log.info("作为客户端连接redis中保存的其他服务器 {}", servers);
                if (!servers.isEmpty()) {
                    for (WebSocketServer server : servers) {
                        if (Objects.equals(webSocketServer.getPort(), server.getPort()) && Objects.equals(webSocketServer.getIp(), server.getIp())) {
                            continue;
                        }
                        ((ServerAsClientAble) webSocketServer).connectServer(webSocketServer, server.getIp(), server.getPort());
                    }
                }

                log.info("将netty服务注册到nacos中 同时将当前netty服务添加到redis中 {}", servers);
                Runnable callback = () -> redisService.addNettyServer(webSocketServer.getIp(), webSocketServer.getPort());
                nacosService.registerInstance(imServerProperties.getNacosNettyName(), webSocketServer.getIp(), imServerProperties.getPort(), "DEFAULT", callback);

                // 定时轮循到nacos 监听是否有新的服务器注册上来 注册上来 进行连接
                scheduledThreadPoolExecutor.scheduleWithFixedDelay(() -> {
                    try {
                        List<Instance> instances = nacosService.getAllInstances(imServerProperties.getNacosNettyName());
                        log.debug("定时获取到nacos上的netty服务 {}", instances);
                        for (Instance instance : instances) {
                            redisService.addNettyServerIfNotExist(instance.getIp(), instance.getPort());
                            if (Objects.equals(webSocketServer.getPort(), instance.getPort()) && Objects.equals(webSocketServer.getIp(), instance.getIp())) {
                                continue;
                            }
                            ((ServerAsClientAble) webSocketServer).connectServer(webSocketServer, instance.getIp(), instance.getPort());
                        }

                    } catch (NacosException e) {

                    }
                }, 0, imServerProperties.getPullNewNettySeconds(), TimeUnit.SECONDS);
            } catch (NacosException e) {
                log.error("启动失败 {}", e.getMessage());
                scheduledThreadPoolExecutor.shutdown();
            }
        };

        webSocketServer.start(startedCallback);
    }
}
