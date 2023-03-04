package com.msb.pay.mq.topic;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.pay.mq.model.PayOrderDelayNotifyMessage;

/**
 * 支付订单延时通知TOPIC
 *
 * @author peng.xy
 * @date 2022/6/10
 */
@Topic("PAY-ORDER-DELAY-NOTIFY-TOPIC")
public interface PayOrderDelayNotifyTopic extends MqTopic<PayOrderDelayNotifyMessage> {

}
