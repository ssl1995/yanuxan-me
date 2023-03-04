package com.msb.mall.comment.enums;

import com.msb.framework.common.model.IDict;

/**
 * 排序规则
 *
 * @author shumengjiao
 */

public enum SortTypeEnum implements IDict<Integer> {
    //
    DEFAULT_SORT(1, "默认排序规则"),
    NEWEST_SORT(2, "最新排序规则"),
    ;

    SortTypeEnum(Integer code, String text) {
        init(code, text);
    }
}
