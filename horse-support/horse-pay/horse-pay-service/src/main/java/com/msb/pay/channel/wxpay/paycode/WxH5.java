package com.msb.pay.channel.wxpay.paycode;

import com.alibaba.fastjson.JSONObject;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.model.v3.H5Info;
import com.ijpay.wxpay.model.v3.SceneInfo;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.msb.pay.channel.IPayCodeService;
import com.msb.pay.channel.wxpay.AbstractWxPaymentService;
import com.msb.pay.channel.wxpay.data.WxMchDataInfo;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.WxH5Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信H5支付方式实现类
 *
 * @author peng.xy
 * @date 2022/6/7
 */
@Slf4j
@Service("padeCode-WX_H5")
public class WxH5 extends AbstractWxPaymentService implements IPayCodeService<WxH5Data> {

    @Override
    public UnifiedOrderRES<WxH5Data> payRequest(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        // 封装请求参数
        UnifiedOrderModel model = super.createUnifiedOrderModel(mchInfo, appInfo, unifiedOrderDTO);
        model.setScene_info(new SceneInfo().setPayer_client_ip(unifiedOrderDTO.getClientIp()).setH5_info(new H5Info().setType("Wap")));
        // 发起支付请求
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        IJPayHttpResponse response = super.v3Post(mchInfo, wxMchDataInfo, WxApiType.H5_PAY, model);
        // 解析响应数据
        String payData = response.getBody();
        WxH5Data payDataInfo = JSONObject.parseObject(payData, WxH5Data.class);
        return new UnifiedOrderRES<WxH5Data>()
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
        return PayCodeEnum.WX_H5;
    }

}
