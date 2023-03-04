package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 支付方式枚举
 */
public enum OrderPayTypeEnum implements IDict<Integer> {

    // 支付方式枚举
    UNPAID(1, "未支付", StringUtils.EMPTY),
    WXPAY(2, "微信支付", "wxpay"),
    ALIPAY(3, "支付宝", "alipay"),
    ;

    private final String mchCode;

    OrderPayTypeEnum(Integer code, String text, String mchCode) {
        init(code, text);
        this.mchCode = mchCode;
    }

    public static int getPayCode(String mchCode) {
        OrderPayTypeEnum[] values = OrderPayTypeEnum.values();
        for (OrderPayTypeEnum orderPayTypeEnum : values) {
            if (Objects.equals(orderPayTypeEnum.getMchCode(), mchCode)) {
                return orderPayTypeEnum.getCode();
            }
        }
        return 0;
    }

    public String getMchCode() {
        return mchCode;
    }

}
