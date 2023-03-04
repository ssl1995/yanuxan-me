package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 订单来源枚举
 */
public enum OrderSourceEnum implements IDict<Integer> {

    // 订单来源枚举
    UNKNOWN(1, "未知来源"),
    ANDROID(2, "安卓端APP"),
    IOS(3, "IOS端APP"),
    H5(4, "H5浏览器"),
    WX(5, "微信浏览器"),
    PC(6, "PC浏览器"),
    ;

    OrderSourceEnum(Integer code, String text) {
        init(code, text);
    }

}
