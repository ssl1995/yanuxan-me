package com.msb.im.convert;

import com.alibaba.fastjson.JSON;
import com.msb.im.module.chat.model.ReqMessage;
import com.msb.im.module.chat.model.RspMessage;
import com.msb.im.netty.ChannelMessageTypeEnum;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

/**
 * 写给客户端的消息或者客服端写给服务器的消息的转换器
 *
 * @author zhoumiao
 * @date 2022-04-13 16:29:34
 */
@Service
public class TextWebSocketFrameConvert {

    public <T> ReqMessage<T> parseClientMessage(TextWebSocketFrame clientMsg) {
        String text = clientMsg.text();
        return JSON.parseObject(text, ReqMessage.class);
    }

    public <T> TextWebSocketFrame createSuccessRspMessage(RspMessage<T> rsp) {
        return new TextWebSocketFrame(JSON.toJSONString(rsp));
    }

    public <T> String createSuccessRspJson(String traceId, Integer traceType, T content) {
        RspMessage<T> successRspMessage = new RspMessage<>();
        successRspMessage.setTraceId(traceId);
        successRspMessage.setTraceType(traceType);
        successRspMessage.setCode(RspMessage.SUCCESS);
        successRspMessage.setContent(content);
        return JSON.toJSONString(successRspMessage);
    }

    public <T> String createSuccessRspJson(String traceId, ChannelMessageTypeEnum traceType, T content) {
        RspMessage<T> successRspMessage = new RspMessage<>();
        successRspMessage.setTraceId(traceId);
        successRspMessage.setTraceType(traceType.getCode());
        successRspMessage.setCode(RspMessage.SUCCESS);
        successRspMessage.setContent(content);
        return JSON.toJSONString(successRspMessage);
    }

    public <T> TextWebSocketFrame createSuccessRspContent(String traceId, Integer traceType, T content) {
        RspMessage<T> successRspMessage = new RspMessage<>();
        successRspMessage.setTraceId(traceId);
        successRspMessage.setTraceType(traceType);
        successRspMessage.setCode(RspMessage.SUCCESS);
        successRspMessage.setContent(content);
        return new TextWebSocketFrame(JSON.toJSONString(successRspMessage));
    }

    public TextWebSocketFrame createSuccessRspContent(String traceId, Integer traceType) {
        RspMessage<Void> successRspMessage = new RspMessage<>();
        successRspMessage.setTraceId(traceId);
        successRspMessage.setTraceType(traceType);
        successRspMessage.setCode(RspMessage.SUCCESS);
        return new TextWebSocketFrame(JSON.toJSONString(successRspMessage));
    }

    public TextWebSocketFrame createErrorRspMessage(String traceId, Integer traceType, String msg) {
        RspMessage<Void> errorReqMessage = new RspMessage<>();
        errorReqMessage.setTraceId(traceId);
        errorReqMessage.setTraceType(traceType);
        errorReqMessage.setCode(RspMessage.ERROR);
        errorReqMessage.setMessage(msg);
        return new TextWebSocketFrame(JSON.toJSONString(errorReqMessage));
    }

    public <T> TextWebSocketFrame createErrorRspMessage(String traceId, Integer traceType, String msg, T t) {
        RspMessage<T> errorReqMessage = new RspMessage<>();
        errorReqMessage.setTraceId(traceId);
        errorReqMessage.setTraceType(traceType);
        errorReqMessage.setCode(RspMessage.ERROR);
        errorReqMessage.setMessage(msg);
        errorReqMessage.setContent(t);
        return new TextWebSocketFrame(JSON.toJSONString(errorReqMessage));
    }

}
