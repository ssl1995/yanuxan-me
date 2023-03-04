package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;
import org.apache.commons.lang3.StringUtils;

/**
 * 订单操作类型枚举
 */
public enum OrderOperationLogTypeEnum implements IDict<Integer> {

    // 订单操作类型枚举
    SUBMIT_ORDER(1, "提交订单", StringUtils.EMPTY),
    TIMEOUT_CANCEL(2, "自动取消", "订单超时未支付，自动取消订单"),
    CANCEL_BY_USER(3, "取消订单", StringUtils.EMPTY),
    CANCEL_BY_ADMIN(4, "后台取消", StringUtils.EMPTY),
    CLOSE_BY_ADMIN(5, "后台关闭", StringUtils.EMPTY),
    PAY_ORDER(6, "支付订单", StringUtils.EMPTY),
    DELIVERY(7, "平台发货", StringUtils.EMPTY),
    USER_RECEIVE(8, "确认收货", StringUtils.EMPTY),
    AUTO_RECEIVE(9, "自动收货", "用户收货超时，自动确认收货"),
    EVALUATE(10, "完成评价", StringUtils.EMPTY),
    UPDATE_RECIPIENT(11, "修改地址", StringUtils.EMPTY),
    UPDATE_AMOUNT(12, "修改费用", StringUtils.EMPTY),
    AUTO_PRAISE(13, "自动好评", "用户超时未评价，自动五星好评")
    ;

    private final String remark;

    OrderOperationLogTypeEnum(Integer code, String text, String remark) {
        this.remark = remark;
        init(code, text);
    }

    public String getRemark() {
        return remark;
    }
}
