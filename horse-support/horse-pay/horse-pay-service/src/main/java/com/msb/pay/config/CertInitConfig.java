package com.msb.pay.config;

import com.msb.framework.common.utils.ListUtil;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.service.AppInfoService;
import com.msb.pay.service.MchInfoService;
import com.msb.pay.channel.context.ChannelContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class CertInitConfig implements ApplicationRunner {

    @Resource
    private MchInfoService mchInfoService;

    @Resource
    private AppInfoService appInfoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 下载商户证书
        this.downloadMchCert();
        // 下载应用证书
        this.downloadAppCert();
    }

    private void downloadMchCert() {
        List<MchInfo> list = mchInfoService.list();
        for (MchInfo mchInfo : list) {
            try {
                IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
                paymentService.downloadMchInfoCert(mchInfo);
            } catch (Exception e) {
                log.error("商户证书下载失败，商户信息：{}", mchInfo, e);
            }
        }
    }

    private void downloadAppCert() {
        List<AppInfo> list = appInfoService.list();
        Map<Long, MchInfo> mchInfoMap = mchInfoService.getMchInfoMap(ListUtil.convertDistinct(list, AppInfo::getMchPrimaryId));
        for (AppInfo appInfo : list) {
            try {
                MchInfo mchInfo = mchInfoMap.get(appInfo.getMchPrimaryId());
                if (Objects.isNull(mchInfo)) {
                    continue;
                }
                IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
                paymentService.downloadAppInfoCert(mchInfo, appInfo);
            } catch (Exception e) {
                log.error("应用证书下载失败，应用信息：{}，", appInfo, e);
            }
        }
    }

}
