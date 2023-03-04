package com.msb.pay.mq.consumer;

import com.msb.framework.mq.annotation.Consumer;
import com.msb.pay.mq.model.PayOrderDelayNotifyMessage;
import com.msb.pay.mq.topic.PayOrderDelayNotifyTopic;
import com.msb.pay.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 支付订单延时通知Consumer
 *
 * @author peng.xy
 * @date 2022/6/10
 */
@Slf4j
@Component
@Consumer
public class PayOrderDelayNotifyConsumer implements PayOrderDelayNotifyTopic {

    @Lazy
    @Resource
    private NotifyService notifyService;

    @Override
    public void consumer(PayOrderDelayNotifyMessage message) {
        notifyService.payOrderNotify(message);
    }

}
