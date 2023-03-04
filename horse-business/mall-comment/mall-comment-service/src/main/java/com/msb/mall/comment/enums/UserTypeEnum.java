package com.msb.mall.comment.enums;

import com.msb.framework.common.model.IDict;

/**
 * 用户类型
 *
 * @author shumengjiao
 */
public enum UserTypeEnum implements IDict<Integer> {
    //
    MERCHANT_USER(1, "商家用户"),
    NORMAL_USER(2, "普通用户");

    UserTypeEnum(Integer code, String text) {
        init(code, text);
    }
}
