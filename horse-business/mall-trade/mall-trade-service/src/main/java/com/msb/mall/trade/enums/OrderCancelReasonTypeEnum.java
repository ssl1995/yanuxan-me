package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 订单取消原因类型枚举
 */
public enum OrderCancelReasonTypeEnum implements IDict<Integer> {

    // 订单取消原因类型枚举
    DONT_WANT_BUY(1, "我不想买了"),
    ADDRESS_ERROR(2, "地址信息填写错误"),
    REDUCE_PRICE(3, "商品降价"),
    OUT_OF_STOCK(4, "商品无货"),
    ELSE_REASON(5, "其它原因"),
    ;

    OrderCancelReasonTypeEnum(Integer code, String text) {
        init(code, text);
    }
}
