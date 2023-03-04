package com.msb.im.netty;

import com.msb.framework.common.model.IDict;

/**
 * 客户端连接的通道类型
 *
 * @author zhou miao
 * @date 2022/04/18
 */
public enum ConnectTypeEnum implements IDict<String> {
    //
    COMMON("1", "普通连接"),
    WAITER("2", "客服连接"),
    INTERNAL("3", "内部连接"),
    ;


    ConnectTypeEnum(String code, String text) {
        init(code, text);
    }

}
