package com.msb.third.enums;

import com.msb.framework.common.model.IDict;

/**
 * 第三方授权枚举
 */
public enum PlatformEnum implements IDict<Integer> {

    /**
     * 微信公众号
     */
    WX_MP(1, "微信公众号"),
    ;

    PlatformEnum(Integer code, String text) {
        init(code, text);
    }
}
