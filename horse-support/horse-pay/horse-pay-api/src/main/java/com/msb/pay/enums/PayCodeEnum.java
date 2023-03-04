package com.msb.pay.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.msb.framework.common.model.IDict;

/**
 * 支付方式枚举类
 *
 * @author peng.xy
 * @date 2022/6/6
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PayCodeEnum implements IDict<String> {

    /**
     * 支付方式
     */
    WX_NATIVE("WX_NATIVE", "微信扫码支付", MchCodeEnum.WXPAY.getCode()),
    WX_H5("WX_H5", "微信H5支付", MchCodeEnum.WXPAY.getCode()),
    WX_JSAPI("WX_JSAPI", "微信公众号支付", MchCodeEnum.WXPAY.getCode()),
    WX_LITE("WX_LITE", "微信小程序支付", MchCodeEnum.WXPAY.getCode()),
    WX_APP("WX_APP", "微信APP支付", MchCodeEnum.WXPAY.getCode()),

    ALI_WAP("ALI_WAP", "支付宝WAP支付", MchCodeEnum.ALIPAY.getCode()),
    ALI_PC("ALI_PC", "支付宝网页支付", MchCodeEnum.ALIPAY.getCode()),
    ALI_QR("ALI_QR", "支付宝二维码付款", MchCodeEnum.ALIPAY.getCode()),
    ALI_APP("ALI_APP", "支付宝APP支付", MchCodeEnum.ALIPAY.getCode()),
    ;

    private final String code;
    private final String text;
    private final String mchCode;

    PayCodeEnum(String code, String text, String mchCode) {
        this.code = code;
        this.text = text;
        this.mchCode = mchCode;
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

    public String getMchCode() {
        return mchCode;
    }
}
