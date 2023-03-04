package com.msb.pay.channel.alipay.paycode;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.ijpay.alipay.AliPayApiConfig;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.alipay.AbstractAliPaymentService;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.AliPcData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝PC网页支付方式实现类
 *
 * @author peng.xy
 * @date 2022/6/7
 */
@Slf4j
@Service("padeCode-ALI_PC")
public class AliPc extends AbstractAliPaymentService implements IPayCodeService<AliPcData> {

    @Override
    public UnifiedOrderRES<AliPcData> payRequest(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        // 支付应用初始化
        AliPayApiConfig aliPayApiConfig = super.apiConfigInit(mchInfo, appInfo);
        // 封装请求参数
        String outTradeNo = super.getOutTradeNo(unifiedOrderDTO.getPayOrderNo(), this.payCode().getCode());
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(outTradeNo);
        model.setSubject(unifiedOrderDTO.getSubject());
        model.setBody(unifiedOrderDTO.getBody());
        model.setTotalAmount(unifiedOrderDTO.getAmount().toString());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setQrPayMode("2");
        String modelJson = JSONObject.toJSONString(model);
        log.info("调用支付宝PC支付参数：{}", modelJson);
        try {
            // 发起支付请求
            String notifyUrl = super.getPayNotifyUrl(unifiedOrderDTO.getPayOrderNo());
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(unifiedOrderDTO.getReturnUrl());
            request.setNotifyUrl(notifyUrl);
            request.setBizModel(model);
            AlipayTradePagePayResponse response = aliPayApiConfig.getAliPayClient().pageExecute(request, "GET");
            // 解析响应数据
            String responseJson = JSONObject.toJSONString(response);
            log.info("调用支付宝PC支付响应：{}", responseJson);
            AliPcData aliPcData = new AliPcData().setPayUrl(response.getBody());
            return new UnifiedOrderRES<AliPcData>()
                    .setMchCode(super.mchCode().getCode())
                    .setAppCode(unifiedOrderDTO.getAppCode())
                    .setAppId(appInfo.getAppId())
                    .setPayCode(unifiedOrderDTO.getPayCode())
                    .setPayData(response.getBody())
                    .setPayDataInfo(aliPcData)
                    .setPayOrderNo(unifiedOrderDTO.getPayOrderNo())
                    .setChannelRequest(modelJson)
                    .setChannelResponse(responseJson);
        } catch (AlipayApiException e) {
            log.error("调用支付宝PC支付失败", e);
            throw new BizException("调用支付宝PC支付失败");
        }
    }

    @Override
    public PayCodeEnum payCode() {
        return PayCodeEnum.ALI_PC;
    }

}
