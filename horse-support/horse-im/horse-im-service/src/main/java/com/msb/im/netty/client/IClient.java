package com.msb.im.netty.client;

import com.msb.im.portobuf.RspMessageProto;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 *
 * @description: ws客户端 通用定义 提供获取客户端ip和端口
 * @author: zhou miao
 * @create: 2022/04/08
 */
public interface IClient {

    /**
     * 客户端端口
     *
     * @return
     */
    int getServerPort();

    /**
     * 客户端ip
     * @return
     */
    String getServerIp();

    /**
     * 打开连接
     */
    boolean open(Runnable openSuccessCallback);

    /**
     * 关闭连接
     */
    void close();

    /**
     * 发送消息
     */
    void sendMessage(RspMessageProto.Model model);
}
