package com.msb.pay.channel;

import com.alibaba.fastjson.JSONObject;
import com.msb.framework.common.exception.BizException;
import com.msb.pay.config.OssConfig;
import com.msb.pay.enums.MchCodeEnum;
import com.msb.pay.model.bo.DownloadCertRES;
import com.msb.pay.model.constant.PayCenterConst;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.File;

/**
 * 支付通道抽象类
 * M：商户资料信息类型
 * A：应用资料信息类型
 *
 * @author peng.xy
 * @date 2022/7/13
 */
@Slf4j
public abstract class AbstractPaymentService<M, A> {

    @Resource
    private OssService ossService;
    @Resource
    private OssConfig ossConfig;

    /**
     * 获取商户资料信息
     *
     * @param mchInfo：商户信息
     * @return M
     * @author peng.xy
     * @date 2022/7/13
     */
    protected M getMchDataInfo(MchInfo mchInfo) {
        return this.parseData(mchInfo.getMchData(), this.mchDataClass());
    }

    /**
     * 获取应用资料信息
     *
     * @param appInfo：应用信息
     * @return A
     * @author peng.xy
     * @date 2022/7/13
     */
    protected A getAppDataInfo(AppInfo appInfo) {
        return this.parseData(appInfo.getAppData(), this.appDataClass());
    }

    /**
     * 解析资料数据
     *
     * @param data：资料
     * @param clas：类型
     * @return T
     * @author peng.xy
     * @date 2022/7/13
     */
    private <T> T parseData(String data, Class<T> clas) {
        if (StringUtils.isNotBlank(data)) {
            return JSONObject.parseObject(data, clas);
        }
        try {
            return clas.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("获取资料信息失败", e);
            throw new BizException("获取资料信息失败");
        }
    }

    /**
     * 获取商户资料类型
     *
     * @return java.lang.Class<M>
     * @author peng.xy
     * @date 2022/7/13
     */
    protected abstract Class<M> mchDataClass();

    /**
     * 获取应用资料类型
     *
     * @return java.lang.Class<A>
     * @author peng.xy
     * @date 2022/7/13
     */
    protected abstract Class<A> appDataClass();

    /**
     * 获取商户代号
     *
     * @return com.msb.pay.enums.MchCodeEnum
     * @author peng.xy
     * @date 2022/7/13
     */
    protected abstract MchCodeEnum mchCode();

    /**
     * 获取微信支付异步通知地址
     *
     * @param payOrderNo：支付订单号
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/6/8
     */
    protected abstract String getPayNotifyUrl(String payOrderNo);

    /**
     * 获取商户订单号
     *
     * @param payOrderNo：支付订单号
     * @param payCode：支付方式
     * @return java.lang.String
     * @author peng.xy
     * @date 2022/7/7
     */
    protected String getOutTradeNo(String payOrderNo, String payCode) {
        return payOrderNo + "-" + payCode;
    }

    /**
     * 下载证书
     *
     * @param mchId：商户ID
     * @param ossUrl：证书OSS路径
     * @param fileName：下载到本地文件名
     * @return com.msb.pay.model.bo.DownloadCertRES
     * @author peng.xy
     * @date 2022/7/13
     */
    protected DownloadCertRES downloadCert(String mchId, String ossUrl, String fileName) {
        String host = ossConfig.getHost().concat("/");
        String dir = PayCenterConst.CERT.LOCAL_DIR.concat(this.mchCode().getCode()).concat("/").concat(mchId);
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String objectPath = ossUrl.replaceAll(host, "");
        String filePath = String.format(dir + "/%s", fileName);
        Boolean downloadResult = ossService.download(objectPath, filePath);
        log.info("下载证书，商户代号：{}，桶路径：{}，本地路径：{}，下载结果：{}", this.mchCode().getCode(), objectPath, filePath, downloadResult);
        return new DownloadCertRES().setOssUrl(ossUrl).setFilePath(filePath).setDownload(downloadResult);
    }

}
