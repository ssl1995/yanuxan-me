package com.msb.pay.channel.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.Payer;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.channel.AbstractPaymentService;
import com.msb.pay.channel.wxpay.config.WxPayConfig;
import com.msb.pay.channel.wxpay.data.WxAppDataInfo;
import com.msb.pay.channel.wxpay.data.WxMchDataInfo;
import com.msb.pay.config.OssConfig;
import com.msb.pay.enums.MchCodeEnum;
import com.msb.pay.model.bo.UnifiedOrderRES;
import com.msb.pay.model.constant.PayCenterConst;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.paydata.WxJsapiData;
import com.msb.pay.service.OssService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付对接抽象类
 *
 * @author peng.xy
 * @date 2022/6/1
 */
@Slf4j
public abstract class AbstractWxPaymentService extends AbstractPaymentService<WxMchDataInfo, WxAppDataInfo> {

    @Resource
    protected WxPayConfig wxPayConfig;
    @Resource
    protected OssService ossService;
    @Resource
    protected OssConfig ossConfig;

    @Override
    protected Class<WxMchDataInfo> mchDataClass() {
        return WxMchDataInfo.class;
    }

    @Override
    protected Class<WxAppDataInfo> appDataClass() {
        return WxAppDataInfo.class;
    }

    @Override
    protected MchCodeEnum mchCode() {
        return MchCodeEnum.WXPAY;
    }

    @Override
    protected String getPayNotifyUrl(String payOrderNo) {
        return wxPayConfig.getPayNotify() + payOrderNo;
    }

    /**
     * bigDecimal金额转分
     *
     * @param amount：金额
     * @return int
     * @author peng.xy
     * @date 2022/6/7
     */
    protected int bigDecimalToInteger(BigDecimal amount) {
        return amount.multiply(new BigDecimal("100")).intValue();
    }

    /**
     * 创建统一下单对象
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param unifiedOrderDTO：下单参数
     * @return com.ijpay.wxpay.model.v3.UnifiedOrderModel
     * @author peng.xy
     * @date 2022/7/15
     */
    protected UnifiedOrderModel createUnifiedOrderModel(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        int total = this.bigDecimalToInteger(unifiedOrderDTO.getAmount());
        String outTradeNo = this.getOutTradeNo(unifiedOrderDTO.getPayOrderNo(), unifiedOrderDTO.getPayCode());
        String notifyUrl = this.getPayNotifyUrl(unifiedOrderDTO.getPayOrderNo());
        return new UnifiedOrderModel()
                .setAppid(appInfo.getAppId())
                .setMchid(mchInfo.getMchId())
                .setDescription(unifiedOrderDTO.getSubject())
                .setOut_trade_no(outTradeNo)
                .setNotify_url(notifyUrl)
                .setAmount(new Amount().setTotal(total).setCurrency(PayCenterConst.CURRENCY.CNY));
    }

    /**
     * 调用微信v3Post接口
     *
     * @param mchInfo：商户信息
     * @param wxMchDataInfo：商户资料
     * @param wxApiType：交易类型枚举
     * @param model：参数
     * @return com.ijpay.core.IJPayHttpResponse
     * @author peng.xy
     * @date 2022/6/7
     */
    protected IJPayHttpResponse v3Post(MchInfo mchInfo, WxMchDataInfo wxMchDataInfo, WxApiType wxApiType, Object model) {
        try {
            log.info("调用微信支付v3post参数：{}", model);
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.POST,
                    WxDomain.CHINA.toString(),
                    wxApiType.toString(),
                    mchInfo.getMchId(),
                    wxMchDataInfo.getSerialNo(),
                    null,
                    wxMchDataInfo.getKeyPath(),
                    JSONObject.toJSONString(model)
            );
            log.info("调用微信支付v3post响应：{}", response);
            if (response.getStatus() == 200) {
                // 签名校验
                boolean verifySignature = WxPayKit.verifySignature(response, wxMchDataInfo.getPlatformCertPath());
                if (!verifySignature) {
                    throw new BizException("微信签名校验失败");
                }
                return response;
            } else {
                throw new BizException("调用微信支付失败");
            }
        } catch (Exception e) {
            log.error("调用微信支付异常", e);
            throw new BizException("调用微信支付异常");
        }
    }

    /**
     * 调用微信v3Get接口
     *
     * @param mchInfo：商户信息
     * @param wxMchDataInfo：商户资料
     * @param urlSuffix：请求类型
     * @param params：参数
     * @return com.ijpay.core.IJPayHttpResponse
     * @author peng.xy
     * @date 2022/6/7
     */
    protected IJPayHttpResponse v3Get(MchInfo mchInfo, WxMchDataInfo wxMchDataInfo, String urlSuffix, Map<String, String> params) {
        try {
            log.info("调用微信支付v3get参数：{}", params);
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.GET,
                    WxDomain.CHINA.toString(),
                    urlSuffix,
                    mchInfo.getMchId(),
                    wxMchDataInfo.getSerialNo(),
                    null,
                    wxMchDataInfo.getKeyPath(),
                    params
            );
            log.info("调用微信支付v3响应：{}", response);
            if (response.getStatus() == 200) {
                // 签名校验
                boolean verifySignature = WxPayKit.verifySignature(response, wxMchDataInfo.getPlatformCertPath());
                if (!verifySignature) {
                    throw new BizException("微信签名校验失败");
                }
                return response;
            } else {
                throw new BizException("调用微信支付失败");
            }
        } catch (Exception e) {
            log.error("调用微信支付异常", e);
            throw new BizException("调用微信支付异常");
        }
    }

    /**
     * JSAPI支付
     *
     * @param mchInfo：商户信息
     * @param appInfo：应用信息
     * @param unifiedOrderDTO：下单参数
     * @return com.msb.pay.model.bo.UnifiedOrderRES<com.msb.pay.model.paydata.WxJsapiData>
     * @author peng.xy
     * @date 2022/7/12
     */
    protected UnifiedOrderRES<WxJsapiData> jsapiPay(MchInfo mchInfo, AppInfo appInfo, UnifiedOrderDTO unifiedOrderDTO) {
        // 封装请求参数
        UnifiedOrderModel model = this.createUnifiedOrderModel(mchInfo, appInfo, unifiedOrderDTO);
        model.setPayer(new Payer().setOpenid(unifiedOrderDTO.getChannelUser()));
        // 发起支付请求
        WxMchDataInfo wxMchDataInfo = this.getMchDataInfo(mchInfo);
        IJPayHttpResponse response = this.v3Post(mchInfo, wxMchDataInfo, WxApiType.JS_API_PAY, model);
        // 解析响应数据
        String prepayId = JSONObject.parseObject(response.getBody()).getString("prepay_id");
        try {
            Map<String, String> sign = WxPayKit.jsApiCreateSign(appInfo.getAppId(), prepayId, wxMchDataInfo.getKeyPath());
            JSONObject payDataJson = new JSONObject(new HashMap<>(sign));
            WxJsapiData payDataInfo = payDataJson.toJavaObject(WxJsapiData.class);
            return new UnifiedOrderRES<WxJsapiData>()
                    .setMchCode(this.mchCode().getCode())
                    .setAppCode(unifiedOrderDTO.getAppCode())
                    .setAppId(appInfo.getAppId())
                    .setPayCode(unifiedOrderDTO.getPayCode())
                    .setPayOrderNo(unifiedOrderDTO.getPayOrderNo())
                    .setPayData(payDataJson.toJSONString())
                    .setPayDataInfo(payDataInfo)
                    .setChannelRequest(JSONObject.toJSONString(model))
                    .setChannelResponse(this.fixResponseJson(response));
        } catch (Exception e) {
            log.error("调用微信支付异常", e);
            throw new BizException("调用微信支付异常");
        }
    }

    /**
     * 修复接口响应json
     *
     * @param response：请求响应
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/6/11
     */
    protected String fixResponseJson(IJPayHttpResponse response) {
        JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(response).replaceAll("null:", "\"http\":"));
        JSONObject body = JSONObject.parseObject(responseJson.getString("body"));
        responseJson.remove("body");
        responseJson.put("body", body);
        return responseJson.toJSONString();
    }

    /**
     * 解析微信回调数据
     *
     * @param request：请求对象
     * @param mchInfo：商户信息
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/6/13
     */
    protected String parseNotify(HttpServletRequest request, MchInfo mchInfo) {
        try {
            // 解析商户资料
            WxMchDataInfo wxMchDataInfo = this.getMchDataInfo(mchInfo);
            // 解析回调参数
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");
            log.info("微信支付回调，timestamp：{}，nonce：{}，serialNo：{}，signature：{}", timestamp, nonce, serialNo, signature);
            String notifyData = HttpKit.readData(request);
            log.info("微信回调通知密文：{}", notifyData);
            String plainText = WxPayKit.verifyNotify(serialNo, notifyData, signature, nonce, timestamp,
                    wxMchDataInfo.getApiKeyV3(), wxMchDataInfo.getPlatformCertPath());
            log.info("微信回调通知明文：{}", plainText);
            return plainText;
        } catch (Exception e) {
            log.error("解析微信回调异常", e);
            throw new BizException("解析微信回调异常");
        }
    }


}
