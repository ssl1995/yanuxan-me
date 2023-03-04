package com.msb.pay.mq.consumer;

import com.msb.framework.mq.annotation.Consumer;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.mq.model.CertSyncNotifyMessage;
import com.msb.pay.mq.topic.AppCertSyncNotifyTopic;
import com.msb.pay.service.AppInfoService;
import com.msb.pay.service.MchInfoService;
import com.msb.pay.channel.context.ChannelContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Component
@Consumer(messageModel = MessageModel.BROADCASTING)
public class AppCertSyncNotifyConsumer implements AppCertSyncNotifyTopic {

    @Resource
    private MchInfoService mchInfoService;
    @Resource
    private AppInfoService appInfoService;

    @Override
    public void consumer(CertSyncNotifyMessage message) {
        log.info("接收应用证书同步消息：{}", message);
        Long appPrimaryId = message.getAppPrimaryId();
        if (Objects.nonNull(appPrimaryId)) {
            // 同步应用证书
            AppInfo appInfo = appInfoService.getByPrimaryId(appPrimaryId);
            MchInfo mchInfo = mchInfoService.getByPrimaryId(appInfo.getMchPrimaryId());
            IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
            paymentService.downloadAppInfoCert(mchInfo, appInfo);
        }
    }

}
