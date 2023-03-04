package com.msb.im.module;

import com.msb.im.netty.ConnectTypeEnum;
import com.msb.im.netty.model.HandshakeParam;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有的channel管理
 */
@Component
public class ChannelManager {
    public static final Map<String, HandshakeParam> handshakeParamMap = new ConcurrentHashMap<>();

    public HandshakeParam remove(Channel channel) {
        return handshakeParamMap.remove(channel.id().asLongText());
    }

    public void add(Channel channel, HandshakeParam handshakeParam) {
        handshakeParamMap.put(channel.id().asLongText(), handshakeParam);
    }

    public boolean isWaiterConnect(Channel channel) {
        HandshakeParam handshakeParam = get(channel);
        return Objects.equals(handshakeParam.getConnect(), ConnectTypeEnum.WAITER.getCode());
    }

    public HandshakeParam get(Channel channel) {
       return handshakeParamMap.get(id(channel));
    }

    private String id(Channel channel) {
        return channel.id().asLongText();
    }
}
