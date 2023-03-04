package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 凭证类型枚举类
 */
public enum EvidenceTypeEnum implements IDict<Integer> {

    // 凭证类型枚举
    APPLY(1, "申请凭证"),
    RETURN(2, "物流凭证"),
    ;

    EvidenceTypeEnum(Integer code, String text) {
        init(code, text);
    }

}
