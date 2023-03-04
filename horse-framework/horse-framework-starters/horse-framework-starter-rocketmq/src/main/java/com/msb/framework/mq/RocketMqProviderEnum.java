package com.msb.framework.mq;

import com.msb.framework.common.model.IDict;

/**
 * @author liao
 */
public enum RocketMqProviderEnum implements IDict<String> {

    //
    OPEN_SOURCE("open_source", "开源rocketmq"),
    ALI_CLOUD("ali_cloud", "阿里云rocketmq");

    private final String code;
    private final String text;

    RocketMqProviderEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }
}
