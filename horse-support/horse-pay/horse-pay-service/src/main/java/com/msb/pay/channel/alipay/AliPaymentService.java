package com.msb.pay.channel.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.BizAssert;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.channel.alipay.data.AliAppDataInfo;
import com.msb.pay.channel.alipay.data.AliMchDataInfo;
import com.msb.pay.enums.RefundStatusEnum;
import com.msb.pay.kit.StrKit;
import com.msb.pay.model.bo.ApplyRefundRES;
import com.msb.pay.model.bo.DownloadCertRES;
import com.msb.pay.model.bo.PayNotifyRES;
import com.msb.pay.model.bo.RefundNotifyRES;
import com.msb.pay.model.constant.PayCenterConst;
import com.msb.pay.model.dto.*;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.model.entity.PayOrder;
import com.msb.pay.model.entity.RefundOrder;
import com.msb.pay.model.vo.AliAppDataVO;
import com.msb.pay.model.vo.AliMchDataVO;
import com.msb.pay.model.vo.BaseAppDataVO;
import com.msb.pay.model.vo.BaseMchDataVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付宝支付Service
 *
 * @author peng.xy
 * @date 2022/6/22
 */
@Slf4j
@Service("payment-alipay")
public class AliPaymentService extends AbstractAliPaymentService implements IPaymentService {

    @Override
    public PayNotifyRES payNotify(HttpServletRequest request, MchInfo mchInfo) {
        Map<String, String> params = AliPayApi.toMap(request);
        log.info("接收支付宝支付通知参数：{}", params);
        AliMchDataInfo aliMchDataInfo = super.getMchDataInfo(mchInfo);
        try {
            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, aliMchDataInfo.getAliPayCertPath(), "UTF-8", "RSA2");
            log.info("解析支付宝支付回调校验：{}", verifyResult);
        } catch (AlipayApiException e) {
            log.error("解析支付宝支付回调异常", e);
            throw new BizException("解析支付宝支付回调异常");
        }
        String tradeStatus = params.get("trade_status");
        log.info("支付宝交易状态：{}", tradeStatus);
        boolean paySuccess = StringUtils.equals("TRADE_SUCCESS", tradeStatus);
        return new PayNotifyRES()
                .setChannelPayOrderNo(params.get("trade_no"))
                .setChannelUserId(params.get("buyer_id"))
                .setChannelNotify(JSONObject.toJSONString(params))
                .setPaySuccess(paySuccess)
                .setSuccessTime(LocalDateTime.now());
    }

    @Override
    public ApplyRefundRES refundRequest(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, ApplyRefundDTO applyRefundDTO) {
        String refundOrderNo = applyRefundDTO.getRefundOrderNo();
        AliPayApiConfig aliPayApiConfig = super.apiConfigInit(mchInfo, appInfo);
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(super.getOutTradeNo(payOrder.getPayOrderNo(), payOrder.getPayCode()));
        model.setTradeNo(payOrder.getChannelPayOrderNo());
        model.setOutRequestNo(refundOrderNo);
        model.setRefundAmount(applyRefundDTO.getRefundAmount().toString());
        model.setRefundReason(applyRefundDTO.getRefundReason());
        String modelJson = JSONObject.toJSONString(model);
        log.info("支付宝发起退款参数：{}", modelJson);

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setNotifyUrl(aliPayConfig.getRefundNotify() + refundOrderNo);
        request.setBizModel(model);

        try {
            AlipayTradeRefundResponse response = aliPayApiConfig.getAliPayClient().certificateExecute(request);
            String responseJson = JSONObject.toJSONString(response);
            log.info("支付宝发起退款响应：{}", responseJson);
            RefundStatusEnum refundStatus = RefundStatusEnum.REFUND_FAIL;
            boolean applySuccess = false;
            if (StringUtils.equals("10000", response.getCode()) && StringUtils.equals("Y", response.getFundChange())) {
                refundStatus = RefundStatusEnum.REFUND_SUCCESS;
                applySuccess = true;
            }
            return new ApplyRefundRES()
                    .setMchCode(this.mchCode().getCode())
                    .setAppCode(applyRefundDTO.getAppCode())
                    .setPayOrderNo(payOrder.getPayOrderNo())
                    .setRefundOrderNo(refundOrderNo)
                    .setPayAmount(payOrder.getAmount())
                    .setRefundAmount(applyRefundDTO.getRefundAmount())
                    .setRefundStatus(refundStatus)
                    .setApplySuccess(applySuccess)
                    .setChannelRequest(modelJson)
                    .setChannelResponse(responseJson);
        } catch (AlipayApiException e) {
            log.error("调用支付宝退款异常", e);
            throw new BizException("调用支付宝退款异常");
        }
    }

    @Override
    public RefundNotifyRES refundNotify(HttpServletRequest request, MchInfo mchInfo) {
        throw new BizException("支付宝未支持退款回调");
    }

    @Override
    public JSONObject tradeQuery(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder) {
        super.apiConfigInit(mchInfo, appInfo);
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(payOrder.getPayOrderNo());
        model.setTradeNo(payOrder.getChannelPayOrderNo());
        try {
            AlipayTradeQueryResponse response = AliPayApi.tradeQueryToResponse(model);
            log.info("调用支付宝交易订单查询结果：{}", response.getBody());
            if (!StringUtils.equals("10000", response.getCode())) {
                throw new BizException(response.getMsg());
            }
            return JSONObject.parseObject(response.getBody());
        } catch (AlipayApiException e) {
            log.error("调用支付宝交易订单查询异常", e);
            throw new BizException("调用支付宝交易订单查询异常");
        }
    }

    @Override
    public JSONObject refundQuery(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, RefundOrder refundOrder) {
        super.apiConfigInit(mchInfo, appInfo);
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setTradeNo(payOrder.getChannelPayOrderNo());
        model.setOutTradeNo(payOrder.getPayOrderNo());
        model.setOutRequestNo(refundOrder.getRefundOrderNo());
        try {
            AlipayTradeFastpayRefundQueryResponse response = AliPayApi.tradeRefundQueryToResponse(model);
            log.info("调用支付宝退款订单查询结果：{}", response.getBody());
            if (!StringUtils.equals("10000", response.getCode())) {
                throw new BizException(response.getMsg());
            }
            return JSONObject.parseObject(response.getBody());
        } catch (AlipayApiException e) {
            log.error("调用支付宝退款订单查询异常", e);
            throw new BizException("调用支付宝退款订单查询异常");
        }
    }

    @Override
    public void parseMchInfoData(MchInfo mchInfo, BaseMchDataVO baseMchDataVO) {
        AliMchDataInfo aliMchDataInfo = super.getMchDataInfo(mchInfo);
        AliMchDataVO aliMchDataVO = new AliMchDataVO()
                .setPublicKey(StringUtils.isNotBlank(aliMchDataInfo.getPublicKey()) ? StrKit.str2Star(aliMchDataInfo.getPublicKey(), 4, 4, 6) : null)
                .setHasAliPayCert(StringUtils.isNotBlank(aliMchDataInfo.getAliPayCertUrl()))
                .setHasAliPayRootCert(StringUtils.isNotBlank(aliMchDataInfo.getAliPayRootCertUrl()));
        baseMchDataVO.setAliMchData(aliMchDataVO);
    }

    @Override
    public boolean saveMchInfoData(MchInfo mchInfo, BaseMchDataDTO baseMchDataDTO) {
        // 获取商户资料
        AliMchDataInfo aliMchDataInfo = super.getMchDataInfo(mchInfo);
        AliMchDataDTO dataDTO = baseMchDataDTO.getAliMchData();
        // 标记证书是否有更新
        boolean isUpdate = false;
        // 支付宝公钥
        if (StringUtils.isNotBlank(dataDTO.getPublicKey())) {
            aliMchDataInfo.setPublicKey(dataDTO.getPublicKey());
        }
        BizAssert.isTrue(StringUtils.isNotBlank(aliMchDataInfo.getPublicKey()), "支付宝商户公钥不能为空");
        // 公钥证书
        if (StringUtils.isNotBlank(dataDTO.getAliPayCertUrl())) {
            DownloadCertRES downloadRES = super.downloadCert(mchInfo.getMchId(), dataDTO.getAliPayCertUrl(), PayCenterConst.CERT.ALI_PAY_CERT_CRT);
            BizAssert.isTrue(downloadRES.isDownload(), "公钥证书下载失败");
            aliMchDataInfo.setAliPayCertUrl(downloadRES.getOssUrl());
            aliMchDataInfo.setAliPayCertPath(downloadRES.getFilePath());
            isUpdate = true;
        }
        BizAssert.isTrue(StringUtils.isNotBlank(aliMchDataInfo.getAliPayCertUrl()), "支付宝公钥证书不能为空");
        // 根证书
        if (StringUtils.isNotBlank(dataDTO.getAliPayRootCertUrl())) {
            DownloadCertRES downloadRES = super.downloadCert(mchInfo.getMchId(), dataDTO.getAliPayRootCertUrl(), PayCenterConst.CERT.ALI_PAY_ROOT_CERT_CRT);
            BizAssert.isTrue(downloadRES.isDownload(), "根证书下载失败");
            aliMchDataInfo.setAliPayRootCertUrl(downloadRES.getOssUrl());
            aliMchDataInfo.setAliPayRootCertPath(downloadRES.getFilePath());
            isUpdate = true;
        }
        BizAssert.isTrue(StringUtils.isNotBlank(aliMchDataInfo.getAliPayRootCertUrl()), "支付宝根证书不能为空");
        // 保存商户数据
        mchInfo.setMchData(JSONObject.toJSONString(aliMchDataInfo));
        return isUpdate;
    }

    @Override
    public void parseAppInfoData(AppInfo appInfo, BaseAppDataVO baseAppDataVO) {
        AliAppDataInfo aliAppDataInfo = super.getAppDataInfo(appInfo);
        AliAppDataVO aliAppDataVO = new AliAppDataVO()
                .setPrivateKey(StringUtils.isNotBlank(aliAppDataInfo.getPrivateKey()) ? StrKit.str2Star(aliAppDataInfo.getPrivateKey(), 4, 4, 6) : null)
                .setHasAppCert(StringUtils.isNotBlank(aliAppDataInfo.getAppCertUrl()));
        baseAppDataVO.setAliAppData(aliAppDataVO);
    }

    @Override
    public boolean saveAppInfoData(MchInfo mchInfo, AppInfo appInfo, BaseAppDataDTO baseAppDataDTO) {
        // 获取应用资料
        AliAppDataInfo aliAppDataInfo = super.getAppDataInfo(appInfo);
        AliAppDataDTO dataDTO = baseAppDataDTO.getAliAppData();
        // 标记证书是否有更新
        boolean isUpdate = false;
        // 应用私钥
        if (StringUtils.isNotBlank(dataDTO.getPrivateKey())) {
            aliAppDataInfo.setPrivateKey(dataDTO.getPrivateKey());
        }
        BizAssert.isTrue(StringUtils.isNotBlank(aliAppDataInfo.getPrivateKey()), "支付宝应用私钥不能为空");
        // 应用证书
        if (StringUtils.isNotBlank(dataDTO.getAppCertUrl())) {
            String appCertName = String.format(PayCenterConst.CERT.ALI_PAY_APP_CERT_CRT, appInfo.getAppId());
            DownloadCertRES downloadRES = super.downloadCert(mchInfo.getMchId(), dataDTO.getAppCertUrl(), appCertName);
            BizAssert.isTrue(downloadRES.isDownload(), "应用下载失败");
            aliAppDataInfo.setAppCertUrl(downloadRES.getOssUrl());
            aliAppDataInfo.setAppCertPath(downloadRES.getFilePath());
            isUpdate = true;
        }
        BizAssert.isTrue(StringUtils.isNotBlank(aliAppDataInfo.getAppCertUrl()), "支付宝应用证书不能为空");
        appInfo.setAppData(JSONObject.toJSONString(aliAppDataInfo));
        return isUpdate;
    }

    @Override
    public void downloadMchInfoCert(MchInfo mchInfo) {
        AliMchDataInfo aliMchDataInfo = super.getMchDataInfo(mchInfo);
        // 下载支付宝公钥证书
        if (StringUtils.isNotBlank(aliMchDataInfo.getAliPayCertUrl())) {
            super.downloadCert(mchInfo.getMchId(), aliMchDataInfo.getAliPayCertUrl(), PayCenterConst.CERT.ALI_PAY_CERT_CRT);
        }
        // 下载支付宝根证书
        if (StringUtils.isNotBlank(aliMchDataInfo.getAliPayRootCertUrl())) {
            super.downloadCert(mchInfo.getMchId(), aliMchDataInfo.getAliPayRootCertUrl(), PayCenterConst.CERT.ALI_PAY_ROOT_CERT_CRT);
        }
    }

    @Override
    public void downloadAppInfoCert(MchInfo mchInfo, AppInfo appInfo) {
        AliAppDataInfo aliAppDataInfo = super.getAppDataInfo(appInfo);
        // 下载应用证书
        if (StringUtils.isNotBlank(aliAppDataInfo.getAppCertUrl())) {
            String appCertName = String.format(PayCenterConst.CERT.ALI_PAY_APP_CERT_CRT, appInfo.getAppId());
            super.downloadCert(mchInfo.getMchId(), aliAppDataInfo.getAppCertUrl(), appCertName);
        }
    }

}
