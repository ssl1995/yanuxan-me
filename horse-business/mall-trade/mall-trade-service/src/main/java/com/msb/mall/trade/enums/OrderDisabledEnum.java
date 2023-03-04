package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 订单禁用枚举
 */
public enum OrderDisabledEnum implements IDict<Integer> {

    // 订单禁用枚举
    NO(0, "未禁用"),
    YES(1, "已禁用"),
    ;

    OrderDisabledEnum(Integer code, String text) {
        init(code, text);
    }

}
