package com.msb.sensitive.enums;

import com.msb.framework.common.model.IDict;

/**
 * @author 86151
 */

public enum EnableTypeEnum implements IDict<Integer> {
    NOT_ENABLE(0, "未启用"),
    ENABLE(1, "已启用")
    ;

    EnableTypeEnum(Integer code, String text) {
        init(code, text);
    }
}
