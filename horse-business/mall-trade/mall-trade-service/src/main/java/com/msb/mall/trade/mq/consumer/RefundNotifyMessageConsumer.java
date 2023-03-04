package com.msb.mall.trade.mq.consumer;

import com.msb.framework.mq.annotation.Consumer;
import com.msb.mall.trade.config.PayCenterConfig;
import com.msb.mall.trade.service.RefundOrderService;
import com.msb.pay.api.PayCenterDubboService;
import com.msb.pay.enums.RefundStatusEnum;
import com.msb.pay.kit.SignKit;
import com.msb.pay.model.dto.RefundNotifyConfirmDTO;
import com.msb.pay.model.dto.RefundNotifyDTO;
import com.msb.pay.mq.topic.RefundNotifyMessageTopic;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Component
@Consumer
public class RefundNotifyMessageConsumer implements RefundNotifyMessageTopic {

    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private PayCenterConfig payCenterConfig;

    @DubboReference
    private PayCenterDubboService payCenterDubboService;

    @Override
    public void consumer(RefundNotifyDTO refundNotifyDTO) {
        log.info("接收到退款成功的MQ消息通知：{}", refundNotifyDTO);
        PayCenterConfig.PayApp payApp = payCenterConfig.getByAppCode(refundNotifyDTO.getAppCode());
        SignKit.signatureValidate(refundNotifyDTO, payApp.getSignKey());
        if (Objects.equals(refundNotifyDTO.getRefundStatus(), RefundStatusEnum.REFUND_SUCCESS.getCode())) {
            boolean result = refundOrderService.refundSuccessCallback(refundNotifyDTO.getRefundOrderNo());
            log.info("执行退款订单回调：{}", result);
        }
        RefundNotifyConfirmDTO refundNotifyConfirmDTO = new RefundNotifyConfirmDTO()
                .setAppCode(refundNotifyDTO.getAppCode())
                .setRefundOrderNo(refundNotifyDTO.getRefundOrderNo());
        SignKit.setSign(refundNotifyConfirmDTO, payApp.getSignKey());
        boolean confirm = payCenterDubboService.refundNotifyConfirm(refundNotifyConfirmDTO);
        log.info("退款成功的MQ消息确认：{}，{}", refundNotifyConfirmDTO, confirm);
    }

}
