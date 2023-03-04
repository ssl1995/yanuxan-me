package com.msb.pay.channel.alipay.paycode;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.ijpay.alipay.AliPayApi;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.alipay.AbstractAliPaymentService;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.AliAppData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付宝APP支付方式实现类
 *
 * @author peng.xy
 * @date 2022/6/7
 */
@Slf4j
@Service("padeCode-ALI_APP")
public class AliApp extends AbstractAliPaymentService implements IPayCodeService<AliAppData> {

    @Override
    public UnifiedOrderRES<AliAppData> payRequest(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        // 支付应用初始化
        super.apiConfigInit(mchInfo, appInfo);
        // 封装请求参数
        String outTradeNo = super.getOutTradeNo(unifiedOrderDTO.getPayOrderNo(), this.payCode().getCode());
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(unifiedOrderDTO.getSubject());
        model.setBody(unifiedOrderDTO.getBody());
        model.setTotalAmount(unifiedOrderDTO.getAmount().toString());
        model.setOutTradeNo(outTradeNo);
        model.setProductCode("QUICK_MSECURITY_PAY");
        String modelJson = JSONObject.toJSONString(model);
        log.info("调用支付宝APP支付参数：{}", modelJson);
        try {
            // 发起支付请求
            String notifyUrl = super.getPayNotifyUrl(unifiedOrderDTO.getPayOrderNo());
            AlipayTradeAppPayResponse response = AliPayApi.appPayToResponse(model, notifyUrl);
            // 解析响应数据
            String responseJson = JSONObject.toJSONString(response);
            log.info("调用支付宝APP支付响应：{}", responseJson);
            AliAppData aliAppData = new AliAppData().setPayData(response.getBody());
            return new UnifiedOrderRES<AliAppData>()
                    .setMchCode(super.mchCode().getCode())
                    .setAppCode(unifiedOrderDTO.getAppCode())
                    .setAppId(appInfo.getAppId())
                    .setPayCode(unifiedOrderDTO.getPayCode())
                    .setPayData(response.getBody())
                    .setPayDataInfo(aliAppData)
                    .setPayOrderNo(unifiedOrderDTO.getPayOrderNo())
                    .setChannelRequest(modelJson)
                    .setChannelResponse(responseJson);
        } catch (AlipayApiException e) {
            log.error("调用支付宝APP支付失败", e);
            throw new BizException("调用支付宝APP支付失败");
        }
    }

    @Override
    public PayCodeEnum payCode() {
        return PayCodeEnum.ALI_APP;
    }

}
