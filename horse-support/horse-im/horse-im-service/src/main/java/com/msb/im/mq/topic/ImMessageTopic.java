package com.msb.im.mq.topic;

import com.msb.framework.common.mq.MqTopic;
import com.msb.framework.common.mq.annotation.Topic;
import com.msb.im.mq.model.ImMqMessage;

/**
 * @author zhou miao
 * @date 2022/04/26
 */
@Topic("IM-MESSAGE")
public interface ImMessageTopic extends MqTopic<ImMqMessage> {

}
