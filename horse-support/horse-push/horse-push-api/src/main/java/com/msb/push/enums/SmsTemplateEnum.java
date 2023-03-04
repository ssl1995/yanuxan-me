package com.msb.push.enums;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author liao
 */
@Getter
public enum SmsTemplateEnum implements Serializable {

    /**
     * 登录验证码模板
     */
    LOGIN_TEMPLATE("login"),
    ;

    private static final long serialVersionUID = 1L;

    private final String messageCode;

    SmsTemplateEnum(String messageCode) {
        this.messageCode = messageCode;
    }
}