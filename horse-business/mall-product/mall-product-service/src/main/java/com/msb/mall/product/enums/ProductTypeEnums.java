package com.msb.mall.product.enums;

import com.msb.framework.common.model.IDict;

/**
 * 商品类型类型枚举
 * @author shumengjiao
 */
public enum ProductTypeEnums implements IDict<Integer> {

    //
    REAL(1, "实物商品"),
    VIRTUAL(2, "虚拟商品"),
    ;

    ProductTypeEnums(Integer code, String text) {
        init(code, text);
    }

}
