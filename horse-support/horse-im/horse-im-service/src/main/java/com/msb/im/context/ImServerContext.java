package com.msb.im.context;

import com.msb.im.netty.client.IClient;
import com.msb.im.netty.server.IServer;
import com.msb.im.netty.server.ServerAsClientAble;
import com.msb.im.redis.RedisService;
import com.msb.im.util.AddressUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * IM上下文，里面包含了一些公共对象
 *
 * @author zhou miao
 * @date 2022/04/18
 */
@Component
public class ImServerContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ImServerContext.applicationContext = applicationContext;
    }


    public static IServer getServer() {
        return applicationContext.getBean(IServer.class);
    }


    public static IClient getClient(String address) {
        IServer server = getServer();
        ServerAsClientAble serverAsClientAble = (ServerAsClientAble) server;
        return serverAsClientAble.getAsClient(address);
    }


    /**
     * @param address
     * @return
     */
    public static boolean isCurrentServer(String address) {
        IServer server = getServer();
        String currentServerAddress = AddressUtil.address(server.getIp(), server.getPort());
        return Objects.equals(currentServerAddress, address);
    }

    public static RedisService getRedisService() {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(RedisService.class);
    }


}
