package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 物流状态枚举
 */
public enum LogisticsStatusEnum implements IDict<Integer> {

    // 物流状态枚举
    NO_DATA(-1, "无数据"),
    ON_PASSAGE(0, "在途"),
    CARGO(1, "揽收"),
    DIFFICULT(2, "疑难"),
    SIGN(3, "签收"),
    BACK(4, "退签"),
    DELIVERY(5, "派件"),
    CUSTOMS_CLEARANCE(8, "清关"),
    REFUSAL(14, "拒签"),
    ;

    LogisticsStatusEnum(Integer code, String text) {
        init(code, text);
    }

}
