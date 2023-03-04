package com.msb.like.api.enums;

import com.msb.framework.common.model.IDict;

/**
 * 系统枚举
 *
 * @author shumengjiao
 */
public enum SystemEnum implements IDict<Long> {
    //
    YANXUAN(1L, "严选商城"),
    ;

    SystemEnum(Long code, String text) {
        init(code, text);
    }
}
