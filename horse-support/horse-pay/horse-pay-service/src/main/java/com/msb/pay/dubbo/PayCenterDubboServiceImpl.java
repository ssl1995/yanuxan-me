package com.msb.pay.dubbo;

import com.msb.pay.api.PayCenterDubboService;
import com.msb.pay.model.dto.*;
import com.msb.pay.model.paydata.PayData;
import com.msb.pay.model.vo.ApplyRefundVO;
import com.msb.pay.model.vo.PrepaymentVO;
import com.msb.pay.model.vo.UnifiedOrderVO;
import com.msb.pay.service.PayOrderService;
import com.msb.pay.service.RefundOrderService;
import com.msb.pay.service.convert.ModelConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@Slf4j
@DubboService
public class PayCenterDubboServiceImpl implements PayCenterDubboService {

    @Resource
    private PayOrderService payOrderService;
    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private ModelConvert modelConvert;

    @Override
    public UnifiedOrderVO<? extends PayData> unifiedOrder(UnifiedOrderDubboDTO unifiedOrderDubboDTO) {
        UnifiedOrderDTO unifiedOrderDTO = modelConvert.toUnifiedOrderDTO(unifiedOrderDubboDTO);
        unifiedOrderDTO.setPayCode(unifiedOrderDubboDTO.getPayCode().getCode());
        return payOrderService.unifiedOrder(unifiedOrderDTO);
    }

    @Override
    public boolean payNotifyConfirm(PayNotifyConfirmDTO payNotifyConfirmDTO) {
        return payOrderService.payNotifyConfirm(payNotifyConfirmDTO);
    }

    @Override
    public ApplyRefundVO applyRefund(ApplyRefundDTO applyRefundDTO) {
        return refundOrderService.applyRefund(applyRefundDTO);
    }

    @Override
    public boolean refundNotifyConfirm(RefundNotifyConfirmDTO refundNotifyConfirmDTO) {
        return refundOrderService.refundNotifyConfirm(refundNotifyConfirmDTO);
    }

    @Override
    public PrepaymentVO prepayment(PrepaymentDTO prepaymentDTO) {
        return payOrderService.prepayment(prepaymentDTO);
    }

}
