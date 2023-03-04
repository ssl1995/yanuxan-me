package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 订单明细状态枚举
 */
public enum OrderProductDetailEnum implements IDict<Integer> {

    // 订单明细状态枚举
    NORMAL(1, "正常"),
    AFTER_SALE(2, "申请售后"),
    REFUND_SUCCESS(3, "退款成功"),
    REFUND_FAIL(4, "退款失败"),
    ;

    OrderProductDetailEnum(Integer code, String text) {
        init(code, text);
    }

}
