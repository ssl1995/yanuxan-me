package com.msb.pay.mq.consumer;

import com.msb.framework.mq.annotation.Consumer;
import com.msb.pay.mq.model.RefundOrderDelayNotifyMessage;
import com.msb.pay.mq.topic.RefundOrderDelayNotifyTopic;
import com.msb.pay.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 退款订单延时通知Consumer
 *
 * @author peng.xy
 * @date 2022/6/10
 */
@Slf4j
@Component
@Consumer
public class RefundOrderDelayNotifyConsumer implements RefundOrderDelayNotifyTopic {

    @Lazy
    @Resource
    private NotifyService notifyService;

    @Override
    public void consumer(RefundOrderDelayNotifyMessage message) {
        notifyService.refundOrderNotify(message);
    }

}
