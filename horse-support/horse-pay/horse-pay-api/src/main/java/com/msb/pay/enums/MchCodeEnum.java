package com.msb.pay.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.msb.framework.common.model.IDict;
import io.swagger.annotations.ApiModel;

/**
 * 支付代号枚举类
 *
 * @author peng.xy
 * @date 2022/6/6
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ApiModel("支付代号枚举类")
public enum MchCodeEnum implements IDict<String> {

    /**
     * 支付代号
     */
    WXPAY("wxpay", "微信支付"),
    ALIPAY("alipay", "支付宝支付"),
    ;

    private final String code;
    private final String text;

    MchCodeEnum(String code, String text) {
        this.code = code;
        this.text = text;
        init(code, text);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }

}
