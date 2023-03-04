package com.msb.pay.channel.wxpay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.kit.AesUtil;
import com.ijpay.core.kit.PayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;
import com.ijpay.wxpay.model.v3.RefundAmount;
import com.ijpay.wxpay.model.v3.RefundModel;
import com.msb.framework.common.exception.BizException;
import com.msb.framework.web.result.BizAssert;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.channel.wxpay.data.WxAppDataInfo;
import com.msb.pay.channel.wxpay.data.WxMchDataInfo;
import com.msb.pay.enums.MchCodeEnum;
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
import com.msb.pay.model.vo.BaseAppDataVO;
import com.msb.pay.model.vo.BaseMchDataVO;
import com.msb.pay.model.vo.WxAppDataVO;
import com.msb.pay.model.vo.WxMchDataVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;

/**
 * 微信支付V3Service
 *
 * @author peng.xy
 * @date 2022/5/25
 */
@Slf4j
@Service("payment-wxpay")
public class WxPaymentV3Service extends AbstractWxPaymentService implements IPaymentService {

    @Override
    public PayNotifyRES payNotify(HttpServletRequest request, MchInfo mchInfo) {
        String plainText = super.parseNotify(request, mchInfo);
        JSONObject notifyJson = JSONObject.parseObject(plainText);
        String tradeState = notifyJson.getString("trade_state");
        log.info("微信交易状态：{}", tradeState);
        boolean paySuccess = StringUtils.equals("SUCCESS", tradeState);
        return new PayNotifyRES()
                .setChannelPayOrderNo(notifyJson.getString("transaction_id"))
                .setChannelUserId(notifyJson.getJSONObject("payer").getString("openid"))
                .setChannelNotify(plainText)
                .setPaySuccess(paySuccess)
                .setSuccessTime(LocalDateTime.now());
    }

    @Override
    public ApplyRefundRES refundRequest(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, ApplyRefundDTO applyRefundDTO) {
        String refundOrderNo = applyRefundDTO.getRefundOrderNo();
        int totalAmount = super.bigDecimalToInteger(payOrder.getAmount());
        int refundAmount = super.bigDecimalToInteger(applyRefundDTO.getRefundAmount());
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        RefundModel model = new RefundModel()
                .setOut_refund_no(refundOrderNo)
                .setOut_trade_no(super.getOutTradeNo(payOrder.getPayOrderNo(), payOrder.getPayCode()))
                .setTransaction_id(payOrder.getChannelPayOrderNo())
                .setReason(applyRefundDTO.getRefundReason())
                .setNotify_url(wxPayConfig.getRefundNotify() + refundOrderNo)
                .setAmount(new RefundAmount().setRefund(refundAmount).setTotal(totalAmount).setCurrency(PayCenterConst.CURRENCY.CNY));
        String modelJson = JSONObject.toJSONString(model);
        log.info("微信发起退款参数：{}", modelJson);
        IJPayHttpResponse response = super.v3Post(mchInfo, wxMchDataInfo, WxApiType.DOMESTIC_REFUNDS, model);
        JSONObject responseJson = JSONObject.parseObject(response.getBody());
        log.info("微信发起退款响应：{}", responseJson);

        String status = responseJson.getString("status");
        RefundStatusEnum refundStatus = RefundStatusEnum.REFUND_FAIL;
        boolean applySuccess = false;
        if (StringUtils.equals("SUCCESS", status) || StringUtils.equals("PROCESSING", status)) {
            refundStatus = RefundStatusEnum.APPLY;
            applySuccess = true;
        }
        return new ApplyRefundRES()
                .setMchCode(MchCodeEnum.WXPAY.getCode())
                .setAppCode(applyRefundDTO.getAppCode())
                .setPayOrderNo(payOrder.getPayOrderNo())
                .setRefundOrderNo(refundOrderNo)
                .setPayAmount(payOrder.getAmount())
                .setRefundAmount(applyRefundDTO.getRefundAmount())
                .setRefundStatus(refundStatus)
                .setApplySuccess(applySuccess)
                .setChannelRefundOrderNo(responseJson.getString("refund_id"))
                .setChannelRequest(modelJson)
                .setChannelResponse(super.fixResponseJson(response));
    }

    @Override
    public RefundNotifyRES refundNotify(HttpServletRequest request, MchInfo mchInfo) {
        String plainText = super.parseNotify(request, mchInfo);
        JSONObject notifyJson = JSONObject.parseObject(plainText);
        String refundStatus = notifyJson.getString("refund_status");
        boolean refundSuccess = StringUtils.equals("SUCCESS", refundStatus);
        return new RefundNotifyRES()
                .setChannelRefundOrderNo(notifyJson.getString("refund_id"))
                .setChannelNotify(plainText)
                .setRefundSuccess(refundSuccess)
                .setSuccessTime(LocalDateTime.now());
    }

    @Override
    public JSONObject tradeQuery(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder) {
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        String urlSuffix = String.format(WxApiType.ORDER_QUERY_BY_ID.toString(), payOrder.getChannelPayOrderNo());
        HashMap<String, String> params = new HashMap<>();
        params.put("mchid", mchInfo.getMchId());
        IJPayHttpResponse response = super.v3Get(mchInfo, wxMchDataInfo, urlSuffix, params);
        return JSONObject.parseObject(response.getBody());
    }

    @Override
    public JSONObject refundQuery(MchInfo mchInfo, AppInfo appInfo, PayOrder payOrder, RefundOrder refundOrder) {
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        String urlSuffix = String.format(WxApiType.DOMESTIC_REFUNDS_QUERY.toString(), refundOrder.getRefundOrderNo());
        IJPayHttpResponse response = super.v3Get(mchInfo, wxMchDataInfo, urlSuffix, Collections.emptyMap());
        return JSONObject.parseObject(response.getBody());
    }

    @Override
    public void parseMchInfoData(MchInfo mchInfo, BaseMchDataVO baseMchDataVO) {
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        WxMchDataVO wxMchDataVO = new WxMchDataVO()
                .setApiKey(StringUtils.isNotBlank(wxMchDataInfo.getApiKey()) ? StrKit.str2Star(wxMchDataInfo.getApiKey(), 4, 4, 6) : null)
                .setApiKeyV3(StringUtils.isNotBlank(wxMchDataInfo.getApiKeyV3()) ? StrKit.str2Star(wxMchDataInfo.getApiKeyV3(), 4, 4, 6) : null)
                .setSerialNo(StringUtils.isNotBlank(wxMchDataInfo.getSerialNo()) ? StrKit.str2Star(wxMchDataInfo.getSerialNo(), 4, 4, 6) : null)
                .setHasKey(StringUtils.isNotBlank(wxMchDataInfo.getKeyOssUrl()))
                .setHasCert(StringUtils.isNotBlank(wxMchDataInfo.getCertOssUrl()))
                .setHasCertP12(StringUtils.isNotBlank(wxMchDataInfo.getCertP12OssUrl()))
                .setHasPlatformCert(StringUtils.isNotBlank(wxMchDataInfo.getPlatformCertOssUrl()));
        baseMchDataVO.setWxMchData(wxMchDataVO);
    }

    @Override
    public boolean saveMchInfoData(MchInfo mchInfo, BaseMchDataDTO baseMchDataDTO) {
        // 获取商户数据
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        WxMchDataDTO dataDTO = baseMchDataDTO.getWxMchData();
        // 标记证书是否有更新
        boolean isUpdate = false;
        // 商户API秘钥
        if (StringUtils.isNotBlank(dataDTO.getApiKey())) {
            wxMchDataInfo.setApiKey(dataDTO.getApiKey());
        }
        BizAssert.isTrue(StringUtils.isNotBlank(wxMchDataInfo.getApiKey()), "微信商户API秘钥不能为空");
        // 商户V3秘钥
        if (StringUtils.isNotBlank(dataDTO.getApiKeyV3())) {
            wxMchDataInfo.setApiKeyV3(dataDTO.getApiKeyV3());
        }
        BizAssert.isTrue(StringUtils.isNotBlank(wxMchDataInfo.getApiKeyV3()), "微信商户v3秘钥不能为空");
        // 证书序列号
        if (StringUtils.isNotBlank(dataDTO.getSerialNo())) {
            wxMchDataInfo.setSerialNo(dataDTO.getSerialNo());
        }
        BizAssert.isTrue(StringUtils.isNotBlank(wxMchDataInfo.getSerialNo()), "证书序列号不能为空");
        // 私钥证书
        if (StringUtils.isNotBlank(dataDTO.getKeyOssUrl())) {
            DownloadCertRES downloadRES = super.downloadCert(mchInfo.getMchId(), dataDTO.getKeyOssUrl(), PayCenterConst.CERT.WX_PAY_KEY_PEM);
            BizAssert.isTrue(downloadRES.isDownload(), "私钥证书下载失败");
            wxMchDataInfo.setKeyOssUrl(downloadRES.getOssUrl());
            wxMchDataInfo.setKeyPath(downloadRES.getFilePath());
            isUpdate = true;
        }
        BizAssert.isTrue(StringUtils.isNotBlank(wxMchDataInfo.getKeyOssUrl()), "私钥证书不能为空");
        // 商户证书
        if (StringUtils.isNotBlank(dataDTO.getCertOssUrl())) {
            DownloadCertRES downloadRES = super.downloadCert(mchInfo.getMchId(), dataDTO.getCertOssUrl(), PayCenterConst.CERT.WX_PAY_CERT_PEM);
            BizAssert.isTrue(downloadRES.isDownload(), "商户证书下载失败");
            wxMchDataInfo.setCertOssUrl(downloadRES.getOssUrl());
            wxMchDataInfo.setCertPath(downloadRES.getFilePath());
            isUpdate = true;
        }
        BizAssert.isTrue(StringUtils.isNotBlank(wxMchDataInfo.getCertOssUrl()), "商户证书不能为空");
        // p12证书
        if (StringUtils.isNotBlank(dataDTO.getCertP12OssUrl())) {
            DownloadCertRES downloadRES = super.downloadCert(mchInfo.getMchId(), dataDTO.getCertP12OssUrl(), PayCenterConst.CERT.WX_PAY_CERT_P12);
            BizAssert.isTrue(downloadRES.isDownload(), "p12证书下载失败");
            wxMchDataInfo.setCertP12OssUrl(downloadRES.getOssUrl());
            wxMchDataInfo.setCertP12Path(downloadRES.getFilePath());
            isUpdate = true;
        }
        BizAssert.isTrue(StringUtils.isNotBlank(wxMchDataInfo.getCertP12OssUrl()), "p12证书不能为空");
        // 生成平台证书
        if (isUpdate) {
//            this.savePlatformCert(mchInfo.getMchId(), wxMchDataInfo);
        }
        // 保存商户资料
        mchInfo.setMchData(JSONObject.toJSONString(wxMchDataInfo));
        return isUpdate;
    }

    @Override
    public void parseAppInfoData(AppInfo appInfo, BaseAppDataVO baseAppDataVO) {
        WxAppDataInfo wxAppDataInfo = super.getAppDataInfo(appInfo);
        WxAppDataVO wxAppDataVO = new WxAppDataVO()
                .setAppSecret(StringUtils.isNotBlank(wxAppDataInfo.getAppSecret()) ? StrKit.str2Star(wxAppDataInfo.getAppSecret(), 4, 4, 6) : null);
        baseAppDataVO.setWxAppData(wxAppDataVO);
    }

    @Override
    public boolean saveAppInfoData(MchInfo mchInfo, AppInfo appInfo, BaseAppDataDTO baseAppDataDTO) {
        WxAppDataInfo wxAppDataInfo = super.getAppDataInfo(appInfo);
        WxAppDataDTO wxAppDataDTO = baseAppDataDTO.getWxAppData();
        if (StringUtils.isNotBlank(wxAppDataDTO.getAppSecret())) {
            wxAppDataInfo.setAppSecret(wxAppDataDTO.getAppSecret());
        }
        appInfo.setAppData(JSONObject.toJSONString(wxAppDataInfo));
        return false;
    }

    @Override
    public void downloadMchInfoCert(MchInfo mchInfo) {
        WxMchDataInfo wxMchDataInfo = super.getMchDataInfo(mchInfo);
        // 下载私钥证书
        if (StringUtils.isNotBlank(wxMchDataInfo.getKeyOssUrl())) {
            super.downloadCert(mchInfo.getMchId(), wxMchDataInfo.getKeyOssUrl(), PayCenterConst.CERT.WX_PAY_KEY_PEM);
        }
        // 下载商户证书
        if (StringUtils.isNotBlank(wxMchDataInfo.getCertOssUrl())) {
            super.downloadCert(mchInfo.getMchId(), wxMchDataInfo.getCertOssUrl(), PayCenterConst.CERT.WX_PAY_CERT_PEM);
        }
        // 下载p12证书
        if (StringUtils.isNotBlank(wxMchDataInfo.getCertP12OssUrl())) {
            super.downloadCert(mchInfo.getMchId(), wxMchDataInfo.getCertP12OssUrl(), PayCenterConst.CERT.WX_PAY_CERT_P12);
        }
        // 下载平台证书
        if (StringUtils.isNotBlank(wxMchDataInfo.getPlatformCertOssUrl())) {
            this.downloadCert(mchInfo.getMchId(), wxMchDataInfo.getPlatformCertOssUrl(), PayCenterConst.CERT.WX_PAY_PLATFORM_PEM);
        }
    }

    @Override
    public void downloadAppInfoCert(MchInfo mchInfo, AppInfo appInfo) {
        // 微信应用不需要证书
    }

    /**
     * 保存微信商户平台证书
     *
     * @param mchId：商户ID
     * @param wxMchDataInfo：微信商户信息
     * @return void
     * @author peng.xy
     * @date 2022/7/9
     */
    private WxMchDataInfo savePlatformCert(String mchId, WxMchDataInfo wxMchDataInfo) {
        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethod.GET,
                    WxDomain.CHINA.toString(),
                    WxApiType.GET_CERTIFICATES.toString(),
                    mchId,
                    wxMchDataInfo.getSerialNo(),
                    null,
                    wxMchDataInfo.getKeyPath(),
                    ""
            );
            String body = response.getBody();
            log.info("保存微信商户平台证书，body: {}，status: {}", body, response.getStatus());

            JSONObject jsonObject = JSONObject.parseObject(body);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            JSONObject encryptObject = dataArray.getJSONObject(0);
            JSONObject encryptCertificate = encryptObject.getJSONObject("encrypt_certificate");
            String associatedData = encryptCertificate.getString("associated_data");
            String cipherText = encryptCertificate.getString("ciphertext");
            String nonce = encryptCertificate.getString("nonce");

            // 平台证书密文解密
            AesUtil aesUtil = new AesUtil(wxMchDataInfo.getApiKeyV3().getBytes(StandardCharsets.UTF_8));
            String publicKey = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), cipherText);

            // 保存证书到本地
            String filePath = PayCenterConst.CERT.LOCAL_DIR + MchCodeEnum.WXPAY.getCode() + "/" + mchId + "/" + PayCenterConst.CERT.WX_PAY_PLATFORM_PEM;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            FileWriter writer = new FileWriter(filePath);
            writer.write(publicKey);
            writer.flush();
            writer.close();

            // 获取平台证书序列号
            X509Certificate certificate = PayKit.getCertificate(new ByteArrayInputStream(publicKey.getBytes()));
            String platSerialNo = certificate.getSerialNumber().toString(16).toUpperCase();
            log.info("平台证书序列号：{}", platSerialNo);

            // 上传证书到OSS
            String platformCertObjectPath = ossConfig.getDir() + mchId + "/" + PayCenterConst.CERT.WX_PAY_PLATFORM_PEM;
            Boolean upload = ossService.upload(platformCertObjectPath, filePath);
            log.info("上传平台证书，桶路径：{}，本地路径：{}，上传结果：{}", platformCertObjectPath, filePath, upload);

            wxMchDataInfo.setPlatformCertOssUrl(ossConfig.getHost() + "/" + platformCertObjectPath);
            wxMchDataInfo.setPlatformCertPath(filePath);
            return wxMchDataInfo;
        } catch (Exception e) {
            log.error("保存微信商户平台证书失败", e);
            throw new BizException("生成微信平台商户证书失败");
        }
    }

}
