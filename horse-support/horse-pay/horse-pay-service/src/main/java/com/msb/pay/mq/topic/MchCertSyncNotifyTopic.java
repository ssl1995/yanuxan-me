package com.msb.pay.mq.topic;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.pay.mq.model.CertSyncNotifyMessage;

/**
 * 商户证书同步TOPIC
 *
 * @author peng.xy
 * @date 2022/6/10
 */
@Topic("MCH_CERT-SYNC-NOTIFY-TOPIC")
public interface MchCertSyncNotifyTopic extends MqTopic<CertSyncNotifyMessage> {

}
