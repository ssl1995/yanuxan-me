package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;
import org.apache.commons.lang3.StringUtils;

/**
 * 退款单操作类型枚举
 */
public enum RefundOperationLogTypeEnum implements IDict<Integer> {

    // 退款单操作类型枚举
    APPLY_REFUND(1, "申请退款", StringUtils.EMPTY),
    UPDATE_BY_USER(2, "修改申请", StringUtils.EMPTY),
    CANCEL_BY_USER(3, "撤销申请", StringUtils.EMPTY),
    AUTO_AGREE_REFUND(4, "自动同意退款", "商家处理超时，自动同意退款"),
    AGREE_REFUND(5, "同意退款", StringUtils.EMPTY),
    DISAGREE_REFUND(6, "拒绝退款", StringUtils.EMPTY),
    AUTO_AGREE_RETURN(7, "自动同意退货", "商家处理超时，自动同意退货"),
    AGREE_RETURN(8, "同意退货", StringUtils.EMPTY),
    DISAGREE_RETURN(9, "拒绝退货", StringUtils.EMPTY),
    AUTO_CLOSE_RECEIVING(10, "自动取消退货", "用户退货超时，自动取消退货"),
    COMPLETE_REFUND(11, "填写退货单", StringUtils.EMPTY),
    AUTO_AGREE_RECEIVING(12, "自动收货退款", "商家处理超时，自动确认收货并退款"),
    AGREE_RECEIVING(13, "同意收货", StringUtils.EMPTY),
    DISAGREE_RECEIVING(14, "拒绝收货", StringUtils.EMPTY),
    REFUND_SUCCESS_NOTIFY(15, "退款成功", StringUtils.EMPTY),
    ;

    private final String remark;

    RefundOperationLogTypeEnum(Integer code, String text, String remark) {
        this.remark = remark;
        init(code, text);
    }

    public String getRemark() {
        return remark;
    }
}
