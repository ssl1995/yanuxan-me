package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 退款单类型枚举
 */
public enum RefundTypeEnum implements IDict<Integer> {

    // 退款单类型
    ONLY_REFUND(1, "仅退款"),
    REFUND_AND_RETURN(2, "退货退款"),
    ;

    RefundTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
