package com.msb.mall.trade.enums;

import com.msb.framework.common.model.IDict;

/**
 * 凭证文件枚举类
 */
public enum EvidenceFileEnum implements IDict<Integer> {

    // 凭证文件枚举
    IMAGE(1, "图片"),
    ;

    EvidenceFileEnum(Integer code, String text) {
        init(code, text);
    }

}
