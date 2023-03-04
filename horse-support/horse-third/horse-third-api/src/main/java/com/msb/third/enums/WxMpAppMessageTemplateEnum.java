package com.msb.third.enums;

import lombok.Getter;

import java.io.Serializable;

/**
 * 公众号模板消息枚举
 */
@Getter
public enum WxMpAppMessageTemplateEnum implements Serializable {

    /**
     * 马士兵严选公众号模板消息
     */
    ORDER_CANCEL("orderCancel", "订单取消提醒"),
    ORDER_PAY("orderPay", "订单支付成功提醒"),
    ORDER_DELIVERY("orderDelivery", "订单发货提醒"),
    RETURN_LOGISTICS("returnLogistics", "售后退货提醒"),
    REFUND_FAIL("refundFail", "退款失败提醒"),
    REFUND_SUCCESS("refundSuccess", "退款成功提醒"),
    ;

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String text;

    WxMpAppMessageTemplateEnum(String code, String text) {
        this.code = code;
        this.text = text;
    }

}
