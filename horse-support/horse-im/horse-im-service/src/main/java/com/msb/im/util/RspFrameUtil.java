package com.msb.im.util;

import com.alibaba.fastjson.JSON;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.portobuf.RspMessageProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 写给客户端的消息或者客服端写给服务器的消息的转换器
 *
 * @author zhoumiao
 * @date 2022-04-13 16:29:34
 */
public class RspFrameUtil {
    private RspFrameUtil() {
    }

    public static BinaryWebSocketFrame createRspFrame(RspMessageProto.Model model) {
        if (model == null) {
            return createRspFrame(null, null, RspMessage.ERROR, "消息为空", null);
        }
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer().writeBytes(model.toByteArray());
        return new BinaryWebSocketFrame(byteBuf);
    }

    public static BinaryWebSocketFrame createRspFrame(String traceId, Integer traceTye, Integer code, String message, Object content) {
        RspMessageProto.Model model = createModel(traceId, traceTye, code, message, content);
        return createRspFrame(model);
    }

    public static RspMessageProto.Model createModel(String traceId, Integer traceTye, Integer code, String message, Object content) {
        RspMessageProto.Model.Builder builder = RspMessageProto.Model.newBuilder();
        if (traceId != null) {
            builder.setTraceId(traceId);
        }
        if (traceTye != null) {
            builder.setTraceType(traceTye);
        }
        if (code != null) {
            builder.setCode(code);
        }
        if (message != null) {
            builder.setMessage(message);
        }
        if (content != null) {
            builder.setContent(JSON.toJSONString(content));
        }
        return builder.build();
    }


}
