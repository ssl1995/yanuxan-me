package com.msb.third.enums;

import com.msb.framework.common.exception.IResultCode;
import com.msb.framework.common.model.IDict;

/**
 * 第三方授权异常枚举
 */
public enum ThirdExceptionCodeEnum implements IResultCode, IDict<String> {

    /**
     * 微信公众号配置异常
     */
    WX_MP_EXCEPTION("WX_MP_EXCEPTION", "微信公众号配置异常"),

    /**
     * 微信公众号消息模板异常
     */
    WX_MP_MESSAGE_TEMPLATE_EXCEPTION("WX_MP_MESSAGE_TEMPLATE_EXCEPTION", "微信公众号消息模板异常"),

    /**
     * 微信APPID配置异常
     */
    WX_APPID_EXCEPTION("WX_APPID_EXCEPTION", "微信APPID配置异常"),

    /**
     * 调用微信API异常
     */
    WX_API_EXCEPTION("WX_API_EXCEPTION", "调用微信API异常"),

    ;

    /**
     * 枚举编号
     */
    private final String code;

    /**
     * 枚举详情
     */
    private final String message;


    /**
     * 构造方法
     *
     * @param code    枚举编号
     * @param message 枚举详情
     */
    ThirdExceptionCodeEnum(String code, String message) {
        init(code, message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
