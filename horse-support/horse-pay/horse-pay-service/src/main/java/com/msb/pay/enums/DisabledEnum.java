package com.msb.pay.enums;

import com.msb.framework.common.model.IDict;

/**
 * 禁用状态枚举
 */
public enum DisabledEnum implements IDict<Boolean> {

    // 禁用状态
    ENABLED(false, "启用"),
    disabled(true, "禁用"),
    ;

    DisabledEnum(Boolean code, String text) {
        init(code, text);
    }

}
