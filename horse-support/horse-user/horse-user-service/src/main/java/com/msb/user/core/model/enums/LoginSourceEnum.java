package com.msb.user.core.model.enums;

import com.msb.framework.common.model.IDict;

public enum LoginSourceEnum implements IDict<Integer> {

    /**
     * 严选商城后台管理系统登录
     */
    MALL_ADMIN(1, "严选后管登录"),

    /**
     * 严选商城app登录
     */
    MALL_APP(2, "严选商城app登录"),
    ;
    LoginSourceEnum(Integer code, String message) {
        init(code, message);
    }
}
