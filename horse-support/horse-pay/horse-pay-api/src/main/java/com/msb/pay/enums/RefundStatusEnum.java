package com.msb.pay.enums;

import com.msb.framework.common.model.IDict;

/**
 * 退款状态枚举类
 *
 * @author peng.xy
 * @date 2022/6/6
 */
public enum RefundStatusEnum implements IDict<Integer> {

    /**
     * 退款状态
     */
    APPLY(1, "退款中"),
    CLOSE(2, "退款关闭"),
    REFUND_SUCCESS(3, "退款成功"),
    REFUND_FAIL(4, "退款失败"),
    ;

    private final Integer code;
    private final String text;

    RefundStatusEnum(Integer code, String text) {
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
