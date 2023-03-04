package com.msb.pay.enums;

import com.msb.framework.common.model.IDict;

/**
 * 通知状态枚举类
 *
 * @author peng.xy
 * @date 2022/6/6
 */
public enum NotifyStatusEnum implements IDict<Integer> {

    /**
     * 通知状态
     */
    UN_NOTIFY(1, "未通知"),
    HAVE_NOTIFY(2, "已通知"),
    HAVE_RESPONSE(3, "已响应"),
    ;

    private final Integer code;
    private final String text;

    NotifyStatusEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
        init(code, text);
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

}
