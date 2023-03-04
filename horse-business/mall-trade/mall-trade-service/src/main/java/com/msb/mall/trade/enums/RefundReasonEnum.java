package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 退款原因枚举
 */
public enum RefundReasonEnum implements IDict<Integer> {

    // 退款原因
    NO_REASON(1, "7天无理由退款"),
    UNSATISFACTORY(2, "不喜欢/效果不满意"),
    INCONFORMITY(3, "商品不符合描述"),
    ELSE_REASON(4, "其它原因"),
    ;

    RefundReasonEnum(Integer code, String text) {
        init(code, text);
    }

}
