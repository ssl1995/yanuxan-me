package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 退款单状态枚举
 */
public enum RefundStatusEnum implements IDict<Integer> {

    // 退款单状态
    APPLY(1, "已申请"),
    CLOSE(2, "已关闭"),
    WAIT_RETURN(3, "待退货"),
    IN_RETURN(4, "退货中"),
    IN_REFUND(5, "退款中"),
    REFUND_SUCCESS(6, "退款成功"),
    REFUND_FAIL(7, "退款失败"),
    ;

    RefundStatusEnum(Integer code, String text) {
        init(code, text);
    }

}
