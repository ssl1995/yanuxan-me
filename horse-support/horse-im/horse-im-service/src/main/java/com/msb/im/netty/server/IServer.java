package com.msb.im.netty.server;

import com.msb.im.netty.ImProperties;

/**
 * @description: 服务器接口
 * @author: zhou miao
 * @create: 2022/04/09
 */
public interface IServer {
    /**
     * 获取server的端口号
     *
     * @return
     */
    int getPort();

    /**
     * 获取server的ip
     *
     * @return
     */
    String getIp();

    /**
     * 启动server服务
     */
    void start(Runnable startedCallback);

    /**
     * 关闭
     */
    void shutdown();

    /**
     * 是否启动
     *
     * @return
     */
    boolean isStart();

    /**
     * 服务器配置
     *
     * @return
     */
    ImProperties getProperties();

    /**
     * 服务器配置
     *
     * @return
     */
    void setProperties(ImProperties imProperties);
}
