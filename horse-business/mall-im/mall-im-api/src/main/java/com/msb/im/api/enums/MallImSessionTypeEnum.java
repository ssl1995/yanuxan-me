package com.msb.im.api.enums;

import com.msb.framework.common.model.IDict;

public enum MallImSessionTypeEnum implements IDict<Integer> {
    SYSTEM_SESSION(1, "系统会话", "{\"type\": \"system\"}")
    ;
    private Integer code;
    private String text;
    private String payload;

    MallImSessionTypeEnum(Integer code, String text, String payload) {
        this.code = code;
        this.text = text;
        this.payload = payload;
        init(code, text);
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }


    public String getPayload() {
        return payload;
    }
}
