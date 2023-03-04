package com.msb.pay.mq.topic;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.pay.model.dto.RefundNotifyDTO;

/**
 * 退款成功TOPIC
 *
 * @author peng.xy
 * @date 2022/6/10
 */
@Topic("REFUND-NOTIFY-TOPIC")
public interface RefundNotifyMessageTopic extends MqTopic<RefundNotifyDTO> {

}
