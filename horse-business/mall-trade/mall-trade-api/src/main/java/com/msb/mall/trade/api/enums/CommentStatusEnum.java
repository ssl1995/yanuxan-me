package com.msb.mall.trade.api.enums;

import com.msb.framework.common.model.IDict;

/**
 * 评论状态枚举
 */
public enum CommentStatusEnum implements IDict<Integer> {

    // 退款单状态
    WAITE_COMMENT(1, "待评价"),
    WAITE_FOLLOW(2, "待追评"),
    FOLLOWED(3, "已追评"),
    ;

    CommentStatusEnum(Integer code, String text) {
        init(code, text);
    }

}
