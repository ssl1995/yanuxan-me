package com.msb.im.netty.service;

import com.msb.framework.redis.RedisClient;
import com.msb.im.netty.ConnectTypeEnum;
import com.msb.im.netty.model.HandshakeParam;
import io.netty.handler.codec.http.FullHttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户连接服务
 *
 * @author zhou miao
 * @date 2022/05/24
 */
@Service
public class UserConnectService {
    private static final String nickname_key = "nickname";
    private static final String avatar_key = "avatar";
    private static final String ticket_key = "ticket";
    private static final String client_key = "client";
    private static final String user_key = "user";
    private static final String connect_key = "connect";

    private static final Class<HandshakeParam> HANDSHAKE_PARAM_CLASS = HandshakeParam.class;

    @Resource
    private RedisClient redisClient;

    public boolean isCommonConnect(Map<String, String> handshakeParamMap) {
        return handshakeParamMap.containsKey(nickname_key)
                && handshakeParamMap.containsKey(avatar_key)
                && handshakeParamMap.containsKey(ticket_key)
                && handshakeParamMap.containsKey(client_key)
                && handshakeParamMap.containsKey(user_key)
                && Objects.equals(handshakeParamMap.get(connect_key), ConnectTypeEnum.COMMON.getCode())
                ;
    }

    public boolean isWaiterConnect(Map<String, String> handshakeParamMap) {
        return handshakeParamMap.containsKey(ticket_key)
                && handshakeParamMap.containsKey(client_key)
                && handshakeParamMap.containsKey(user_key)
                && Objects.equals(handshakeParamMap.get(connect_key), ConnectTypeEnum.WAITER.getCode())
                ;
    }

    public boolean isInternalConnect(Map<String, String> handshakeParamMap) {
        return Objects.equals(handshakeParamMap.get(connect_key), ConnectTypeEnum.INTERNAL.getCode())
                ;
    }

    /**
     * 解析uri中的参数
     *
     * @param handShakeMapParam 握手的参数
     * @return 握手的参数
     */
    public HandshakeParam getHandshakeParam(Map<String, String> handShakeMapParam) {
        HandshakeParam handshakeParam = new HandshakeParam();
        handShakeMapParam.forEach((k, v) -> {
            try {
                Field declaredField = HANDSHAKE_PARAM_CLASS.getDeclaredField(k);
                declaredField.setAccessible(true);
                declaredField.set(handshakeParam, v);
            } catch (NoSuchFieldException | IllegalAccessException e) {

            }
        });
        return handshakeParam;
    }

    public Map<String, String> handShakeMapParam(FullHttpRequest fullHttpRequest) {
        String handshakeParams = fullHttpRequest.uri();
        try {
            String decode = URLDecoder.decode(handshakeParams, "UTF-8");
            String[] uriArray = decode.split("\\?");
            String[] params = uriArray[1].split("&");
            Map<String, String> handShakeMapParam = new HashMap<>();
            for (String param : params) {
                int indexOf = param.indexOf("=");
                String k = param.substring(0, indexOf);
                String v = param.substring(indexOf + 1);
                handShakeMapParam.put(k, v);
            }
            return handShakeMapParam;
        } catch (UnsupportedEncodingException e) {
            return Collections.emptyMap();
        }
    }

}
