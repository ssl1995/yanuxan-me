package com.msb.im.util;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import static com.msb.im.netty.ImServerProperties.ADDRESS_SPLIT;
import static com.msb.im.netty.ImServerProperties.WS_PROTOCOL;

/**
 * @description:
 * @author: zhou miao
 * @create: 2022/04/09
 */
public class AddressUtil {

    private AddressUtil() {}

    public static String remoteIp(Channel channel) {
        if (channel == null || !channel.isActive()) {
            return null;
        }
        return ((InetSocketAddress)channel.remoteAddress()).getAddress().getHostAddress();
    }

    public static int remotePort(Channel channel) {
        if (channel == null) {
            return -1;
        }
        return ((InetSocketAddress)channel.remoteAddress()).getPort();
    }

    public static String localIp(Channel channel) {
        if (channel == null) {
            return null;
        }
        return ((InetSocketAddress)channel.localAddress()).getAddress().getHostAddress();
    }

    public static int localPort(Channel channel) {
        if (channel == null) {
            return -1;
        }
        return ((InetSocketAddress)channel.localAddress()).getPort();
    }

    /**
     *
     * @param channel
     * @return ip:port
     */
    public static String localAddress(Channel channel) {
        String ip = localIp(channel);
        int port = localPort(channel);
        return ip + ADDRESS_SPLIT + port;
    }

    /**
     *
     * @param ip
     * @param port
     * @return ip:port
     */
    public static String address(String ip, int port) {
        return ip + ADDRESS_SPLIT + port;
    }

    /**
     *
     * @param channel
     * @return ws://ip:port
     */
    public static String wsServerAddress(Channel channel) {
        String ip = localIp(channel);
        int port = localPort(channel);
        return WS_PROTOCOL + ip + ADDRESS_SPLIT + port;
    }

    /**
     * ip:port
     * 解析ip
     *
     * @param address
     * @return
     */
    public static String parseIp(String address) {
        String[] split = address.split(ADDRESS_SPLIT);
        return split[0];
    }

    /**
     * ip:port
     * 解析端口
     *
     * @param address
     * @return
     */
    public static int parsePort(String address) {
        String[] split = address.split(ADDRESS_SPLIT);
        return Integer.parseInt(split[1]);

    }

    public static String remoteAddress(Channel channel) {
        if (channel != null) {
            String ip = remoteIp(channel);
            int port = remotePort(channel);
            return address(ip, port);
        }

        return null;
    }
}
