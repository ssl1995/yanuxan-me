package com.msb.mall.trade.exception;

import com.msb.framework.common.exception.IResultCode;
import com.msb.framework.common.model.IDict;

public enum TradeExceptionCodeEnum implements IResultCode, IDict<String> {

    /**
     * 订单信息异常
     */
    ORDER_EXCEPTION("ORDER_EXCEPTION", "订单信息异常"),

    /**
     * 订单状态异常
     */
    ORDER_STATUS_EXCEPTION("ORDER_STATUS_EXCEPTION", "订单状态异常"),

    /**
     * 用户信息异常
     */
    USER_EXCEPTION("USER_EXCEPTION", "用户信息异常"),

    /**
     * 订单商品信息错误
     */
    ORDER_PRODUCT_ERROR("ORDER_PRODUCT_EXCEPTION", "商品信息错误"),

    /**
     * 订单商品状态异常
     */
    ORDER_PRODUCT_STATUS_EXCEPTION("ORDER_PRODUCT_STATUS_EXCEPTION", "订单商品状态异常"),

    /**
     * 订单商品活动类型异常
     */
    ORDER_PRODUCT_ACTIVITY_EXCEPTION("ORDER_PRODUCT_ACTIVITY_EXCEPTION", "订单商品活动类型异常"),

    /**
     * 订单物流信息错误
     */
    ORDER_LOGISTICS_ERROR("ORDER_LOGISTICS_ERROR", "订单物流信息错误"),

    /**
     * 订单金额异常
     */
    ORDER_AMOUNT_EXCEPTION("ORDER_AMOUNT_EXCEPTION", "订单金额异常"),

    /**
     * 订单信息保存失败
     */
    ORDER_SAVE_FAIL("ORDER_SAVE_FAIL", "订单信息保存失败"),

    /**
     * 订单支付信息保存失败
     */
    ORDER_PAY_SAVE_FAIL("ORDER_PAY_SAVE_FAIL", "订单支付信息保存失败"),

    /**
     * 订单支付信息异常
     */
    ORDER_PAY_DATA_EXCEPTION("ORDER_PAY_DATA_EXCEPTION", "订单支付信息异常"),

    /**
     * 订单支付信息更新失败
     */
    ORDER_PAY_UPDATE_FAIL("ORDER_PAY_UPDATE_FAIL", "订单支付信息更新失败"),

    /**
     * 订单支付超时
     */
    ORDER_PAY_EXPIRE("ORDER_PAY_EXPIRE", "订单支付超时，请重新下单"),
    /**
     * 订单信息更新失败
     */
    ORDER_UPDATE_FAIL("ORDER_UPDATE_FAIL", "订单信息更新失败"),

    /**
     * 订单商品信息保存失败
     */
    ORDER_PRODUCT_SAVE_FAIL("ORDER_PRODUCT_SAVE_FAIL", "订单商品信息保存失败"),

    /**
     * 订单物流信息保存失败
     */
    ORDER_LOGISTICS_SAVE_FAIL("ORDER_LOGISTICS_SAVE_FAIL", "订单物流信息保存失败"),

    /**
     * 订单日志信息保存失败
     */
    ORDER_LOG_SAVE_FAIL("ORDER_LOG_SAVE_FAIL", "订单日志信息保存失败"),

    /**
     * 快递100API查询调用错误
     */
    EXPRESS100_QUERY_API_ERROR("EXPRESS100_QUERY_API_ERROR", "快递100API调用错误"),

    /**
     * 快递100API订阅调用错误
     */
    EXPRESS100_SUBSCRIBE_API_ERROR("EXPRESS100_SUBSCRIBE_API_ERROR", "快递100API调用错误"),

    /**
     * 购物车数据异常
     */
    SHOPPING_CART_ERROR("SHOPPING_CART_ERROR", "购物车数据异常"),

    /**
     * 收货地址数据异常
     */
    RECIPIENT_ADDRESS_ERROR("RECIPIENT_ADDRESS_ERROR", "收货地址数据异常"),

    /**
     * 退款单信息异常
     */
    REFUND_EXCEPTION("REFUND_EXCEPTION", "退款单信息异常"),

    /**
     * 无退款单信息
     */
    NO_REFUND_EXCEPTION("REFUND_EXCEPTION", "无售后单信息"),

    /**
     * 退款单状态异常
     */
    REFUND_STATUS_EXCEPTION("REFUND_STATUS_EXCEPTION", "退款单状态异常"),

    /**
     * 退款单类型异常
     */
    REFUND_TYPE_EXCEPTION("REFUND_TYPE_EXCEPTION", "退款单类型异常"),

    /**
     * 退款单信息保存失败
     */
    REFUND_SAVE_FAIL("REFUND_SAVE_FAIL", "退款单信息保存失败"),

    /**
     * 退款单信息修改失败
     */
    REFUND_UPDATE_FAIL("REFUND_UPDATE_FAIL", "退款单信息修改失败"),

    /**
     * 退款单凭证保存失败
     */
    REFUND_EVIDENCE_SAVE_FAIL("REFUND_EVIDENCE_SAVE_FAIL", "退款单凭证保存失败"),

    /**
     * 退款单日志保存失败
     */
    REFUND_LOG_SAVE_FAIL("REFUND_LOG_SAVE_FAIL", "退款单日志保存失败"),

    /**
     * 退款单物流信息保存失败
     */
    REFUND_LOGISTICS_SAVE_FAIL("REFUND_LOGISTICS_SAVE_FAIL", "退款单物流信息保存失败"),

    /**
     * 退款单物流信息异常
     */
    REFUND_LOGISTICS_EXCEPTION("REFUND_LOGISTICS_EXCEPTION", "退款单物流信息异常"),

    /**
     * 发起退款失败
     */
    REFUND_REQUEST_FAIL("REFUND_REQUEST_FAIL", "发起退款失败"),

    /**
     * 退款请求信息保存失败
     */
    REFUND_REQUEST_SAVE_FAIL("REFUND_REQUEST_SAVE_FAIL", "退款请求信息保存失败"),

    /**
     * 退款请求信息更新失败
     */
    REFUND_REQUEST_UPDATE_FAIL("REFUND_REQUEST_UPDATE_FAIL", "退款请求信息更新失败"),

    /**
     * 退款请求信息异常
     */
    REFUND_REQUEST_DATA_EXCEPTION("REFUND_REQUEST_DATA_EXCEPTION", "退款请求信息异常"),

    /**
     * 微信APPID配置异常
     */
    WX_APPID_EXCEPTION("WX_APPID_EXCEPTION", "微信APPID配置异常"),

    /**
     * 调用微信API异常
     */
    WX_API_EXCEPTION("WX_API_EXCEPTION", "调用微信API异常"),
    ;

    /**
     * 枚举编号
     */
    private final String code;

    /**
     * 枚举详情
     */
    private final String message;


    /**
     * 构造方法
     *
     * @param code    枚举编号
     * @param message 枚举详情
     */
    TradeExceptionCodeEnum(String code, String message) {
        init(code, message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
