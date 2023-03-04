package com.msb.pay.channel.wxpay.paycode;

import com.alibaba.fastjson.JSONObject;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.wxpay.AbstractWxPaymentService;
import com.msb.pay.channel.wxpay.data.WxMchDataInfo;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.WxNativeData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信扫码支付方式实现类
 *
 * @author peng.xy
 * @date 2022/6/7
 */
@Slf4j
@Service("padeCode-WX_NATIVE")
public class WxNative extends AbstractWxPaymentService implements IPayCodeService<WxNativeData> {

    @Override
    public UnifiedOrderRES<WxNativeData> payRequest(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        // 封装请求参数
        UnifiedOrderModel model = super.createUnifiedOrderModel(mchInfo, appInfo, unifiedOrderDTO);
        // 发起支付请求
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        IJPayHttpResponse response = super.v3Post(mchInfo, wxMchDataInfo, WxApiType.NATIVE_PAY, model);
        // 解析响应数据
        String payData = response.getBody();
        WxNativeData payDataInfo = JSONObject.parseObject(payData, WxNativeData.class);
        return new UnifiedOrderRES<WxNativeData>()
                .setMchCode(super.mchCode().getCode())
                .setAppCode(unifiedOrderDTO.getAppCode())
                .setAppId(appInfo.getAppId())
                .setPayCode(unifiedOrderDTO.getPayCode())
                .setPayOrderNo(unifiedOrderDTO.getPayOrderNo())
                .setPayData(payData)
                .setPayDataInfo(payDataInfo)
                .setChannelRequest(JSONObject.toJSONString(model))
                .setChannelResponse(super.fixResponseJson(response));
    }

    @Override
    public PayCodeEnum payCode() {
        return PayCodeEnum.WX_NATIVE;
    }

}
