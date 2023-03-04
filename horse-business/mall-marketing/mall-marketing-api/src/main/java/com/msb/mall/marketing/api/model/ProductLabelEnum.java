package com.msb.mall.marketing.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.msb.framework.common.model.IDict;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProductLabelEnum implements IDict<String> {

    // 活动标签
    SECOND_KILL("second_kill", "秒杀"),
    RECOMMENDED("recommended", "推荐"),
    ;

    ProductLabelEnum(String code, String text) {
        init(code, text);
    }
}
