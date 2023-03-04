package com.msb.pay.mq.consumer;

import com.msb.framework.mq.annotation.Consumer;
import com.msb.pay.channel.IPaymentService;
import com.msb.pay.model.entity.MchInfo;
import com.msb.pay.mq.model.CertSyncNotifyMessage;
import com.msb.pay.mq.topic.MchCertSyncNotifyTopic;
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
public class MchCertSyncNotifyConsumer implements MchCertSyncNotifyTopic {

    @Resource
    private MchInfoService mchInfoService;

    @Override
    public void consumer(CertSyncNotifyMessage message) {
        log.info("接收商户证书同步消息：{}", message);
        Long mchPrimaryId = message.getMchPrimaryId();
        if (Objects.nonNull(mchPrimaryId)) {
            // 同步商户证书
            MchInfo mchInfo = mchInfoService.getByPrimaryId(mchPrimaryId);
            IPaymentService paymentService = ChannelContext.getIPaymentService(mchInfo.getMchCode());
            paymentService.downloadMchInfoCert(mchInfo);
        }
    }

}
