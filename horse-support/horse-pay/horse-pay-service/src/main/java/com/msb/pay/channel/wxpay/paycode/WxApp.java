package com.msb.pay.channel.wxpay.paycode;

import com.alibaba.fastjson.JSONObject;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.wxpay.AbstractWxPaymentService;
import com.msb.pay.channel.wxpay.data.WxMchDataInfo;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.WxAppData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信APP支付方式实现类
 *
 * @author peng.xy
 * @date 2022/6/7
 */
@Slf4j
@Service("padeCode-WX_APP")
public class WxApp extends AbstractWxPaymentService implements IPayCodeService<WxAppData> {

    @Override
    public UnifiedOrderRES<WxAppData> payRequest(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        // 封装请求参数
        UnifiedOrderModel model = super.createUnifiedOrderModel(mchInfo, appInfo, unifiedOrderDTO);
        // 发起支付请求
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        IJPayHttpResponse response = super.v3Post(mchInfo, wxMchDataInfo, WxApiType.APP_PAY, model);
        // 解析响应数据
        try {
            String prepayId = JSONObject.parseObject(response.getBody()).getString("prepay_id");
            Map<String, String> sign = WxPayKit.appCreateSign(appInfo.getAppId(), mchInfo.getMchId(), prepayId, wxMchDataInfo.getKeyPath());
            JSONObject payDataJson = new JSONObject(new HashMap<>(sign));
            WxAppData payDataInfo = payDataJson.toJavaObject(WxAppData.class);
            return new UnifiedOrderRES<WxAppData>()
                    .setMchCode(super.mchCode().getCode())
                    .setAppCode(unifiedOrderDTO.getAppCode())
                    .setAppId(appInfo.getAppId())
                    .setPayCode(unifiedOrderDTO.getPayCode())
                    .setPayOrderNo(unifiedOrderDTO.getPayOrderNo())
                    .setPayData(payDataJson.toJSONString())
                    .setPayDataInfo(payDataInfo)
                    .setChannelRequest(JSONObject.toJSONString(model))
                    .setChannelResponse(super.fixResponseJson(response));
        } catch (Exception e) {
            log.error("调用微信支付异常", e);
            throw new BizException("调用微信支付异常");
        }
    }

    @Override
    public PayCodeEnum payCode() {
        return PayCodeEnum.WX_APP;
    }

}
