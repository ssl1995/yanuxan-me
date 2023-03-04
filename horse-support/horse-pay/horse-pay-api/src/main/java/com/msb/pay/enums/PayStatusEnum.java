package com.msb.pay.enums;

import com.msb.framework.common.model.IDict;

/**
 * 支付状态枚举类
 *
 * @author peng.xy
 * @date 2022/6/6
 */
public enum PayStatusEnum implements IDict<Integer> {

    /**
     * 支付状态
     */
    PREPAY(0, "预支付"),
    UNPAID(1, "支付中"),
    CLOSE(2, "已关闭"),
    PAY_SUCCESS(3, "支付成功"),
    PAY_FAIL(4, "支付失败"),
    PORTION_REFUND(5, "部分退款"),
    FULL_REFUND(6, "全额退款"),
    ;

    private final Integer code;
    private final String text;

    PayStatusEnum(Integer code, String text) {
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
