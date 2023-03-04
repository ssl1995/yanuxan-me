package com.msb.mall.comment.enums;

import com.msb.framework.common.model.IDict;

/**
 * 订单状态枚举
 */
public enum OrderStatusEnum implements IDict<Integer> {

    // 订单状态枚举
    UNPAID(1, "待支付"),
    CLOSE(2, "已关闭"),
    PAID(3, "已支付"),
    DELIVERED(4, "已发货"),
    RECEIVING(5, "已收货"),
    FINISH(6, "已完成"),
    APPENDED(7, "已追评"),
    ;

    OrderStatusEnum(Integer code, String text) {
        init(code, text);
    }

}
