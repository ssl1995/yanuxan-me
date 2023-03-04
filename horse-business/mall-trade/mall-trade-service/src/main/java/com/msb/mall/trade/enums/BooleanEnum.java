package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 布尔值枚举
 */
public enum BooleanEnum implements IDict<Boolean> {

    // 退款单状态
    NO(false, "否"),
    YES(true, "是"),
    ;

    BooleanEnum(Boolean code, String text) {
        init(code, text);
    }

}
