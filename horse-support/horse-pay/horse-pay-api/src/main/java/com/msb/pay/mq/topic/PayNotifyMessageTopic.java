package com.msb.pay.mq.topic;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.pay.model.dto.PayNotifyDTO;

/**
 * 支付成功TOPIC
 *
 * @author peng.xy
 * @date 2022/6/10
 */
@Topic("PAY-NOTIFY-TOPIC")
public interface PayNotifyMessageTopic extends MqTopic<PayNotifyDTO> {

}
