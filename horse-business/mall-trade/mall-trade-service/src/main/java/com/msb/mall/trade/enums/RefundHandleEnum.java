package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 退款处理状态枚举
 */
public enum RefundHandleEnum implements IDict<Integer> {

    // 商家处理状态
    WAIT_HANDLE(1, "待处理"),
    AGREE_REFUND(2, "同意退款"),
    DISAGREE_REFUND(3, "拒绝退款"),
    AGREE_RETURN(4, "同意退货"),
    DISAGREE_RETURN(5, "拒绝退货"),
    AGREE_RECEIVING(6, "确认收货"),
    DISAGREE_RECEIVING(7, "拒绝收货"),
    ;

    RefundHandleEnum(Integer code, String text) {
        init(code, text);
    }

}
