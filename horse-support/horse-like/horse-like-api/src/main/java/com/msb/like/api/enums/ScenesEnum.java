package com.msb.like.api.enums;

import com.msb.framework.common.model.IDict;

/**
 * 场景枚举
 *
 * @author shumengjiao
 */
public enum ScenesEnum implements IDict<Long> {
    //
    PRODUCT_COMMENT(new Long(1), "商品评论"),
    ;

    ScenesEnum(Long code, String text) {
        init(code, text);
    }
}
