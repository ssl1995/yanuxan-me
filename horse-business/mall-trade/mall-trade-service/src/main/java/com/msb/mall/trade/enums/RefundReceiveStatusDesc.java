package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 退款收货状态枚举
 */
public enum RefundReceiveStatusDesc implements IDict<Integer> {

    // 退款收货状态
    NOT_RECEIVING(1, "未收到货"),
    IS_RECEIVING(2, "已收到货"),
    ;

    RefundReceiveStatusDesc(Integer code, String text) {
        init(code, text);
    }

}
