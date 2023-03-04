package com.msb.im.api.dto;

import com.msb.framework.common.model.IDict;

/**
 * 严选im 自定义消息类型
 */
public enum MallImCustomMessageType implements IDict<String> {
    //
    TRANSFER_WAITER_SESSION("transferWaiterSession", "转移客服会话"),
    ;

    private String code;
    private String text;

    MallImCustomMessageType(String code, String text) {
        this.code = code;
        this.text = text;
        init(code, text);
    }
}
