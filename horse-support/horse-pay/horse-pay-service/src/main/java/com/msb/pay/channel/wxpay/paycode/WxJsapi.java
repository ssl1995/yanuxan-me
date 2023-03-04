package com.msb.pay.channel.wxpay.paycode;

import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.wxpay.AbstractWxPaymentService;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.WxJsapiData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信公众号支付方式实现类
 *
 * @author peng.xy
 * @date 2022/6/7
 */
@Slf4j
@Service("padeCode-WX_JSAPI")
public class WxJsapi extends AbstractWxPaymentService implements IPayCodeService<WxJsapiData> {

    @Override
    public UnifiedOrderRES<WxJsapiData> payRequest(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        return super.jsapiPay(mchInfo, appInfo, unifiedOrderDTO);
    }

    @Override
    public PayCodeEnum payCode() {
        return PayCodeEnum.WX_JSAPI;
    }

}
