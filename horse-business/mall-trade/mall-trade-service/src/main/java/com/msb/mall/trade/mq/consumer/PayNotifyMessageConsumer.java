package com.msb.mall.trade.mq.consumer;

import com.msb.framework.mq.annotation.Consumer;
import com.msb.mall.trade.config.PayCenterConfig;
import com.msb.mall.trade.service.TradeOrderService;
import com.msb.pay.api.PayCenterDubboService;
import com.msb.pay.enums.PayStatusEnum;
import com.msb.pay.kit.SignKit;
import com.msb.pay.model.dto.PayNotifyConfirmDTO;
import com.msb.pay.model.dto.PayNotifyDTO;
import com.msb.pay.mq.topic.PayNotifyMessageTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Component
@Consumer
public class PayNotifyMessageConsumer implements PayNotifyMessageTopic {

    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private PayCenterConfig payCenterConfig;

    @DubboReference
    private PayCenterDubboService payCenterDubboService;

    @Override
    public void consumer(PayNotifyDTO payNotifyDTO) {
        log.info("接收到支付成功的MQ消息通知：{}", payNotifyDTO);
        PayCenterConfig.PayApp payApp = payCenterConfig.getByPayCode(payNotifyDTO.getPayCode());
        SignKit.signatureValidate(payNotifyDTO, payApp.getSignKey());
        if (Objects.equals(payNotifyDTO.getPayStatus(), PayStatusEnum.PAY_SUCCESS.getCode())) {
            boolean result = tradeOrderService.paySuccessCallback(payNotifyDTO);
            log.info("执行交易订单回调：{}", result);
        }
        PayNotifyConfirmDTO payNotifyConfirmDTO = new PayNotifyConfirmDTO()
                .setAppCode(payNotifyDTO.getAppCode())
                .setPayOrderNo(payNotifyDTO.getPayOrderNo());
        SignKit.setSign(payNotifyConfirmDTO, payApp.getSignKey());
        boolean confirm = payCenterDubboService.payNotifyConfirm(payNotifyConfirmDTO);
        log.info("支付成功的MQ消息确认：{}，{}", payNotifyConfirmDTO, confirm);
    }

}
