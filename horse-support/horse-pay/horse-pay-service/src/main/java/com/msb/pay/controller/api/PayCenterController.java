package com.msb.pay.controller.api;

import com.msb.framework.web.transform.annotation.Transform;
import com.msb.pay.model.dto.ApplyRefundDTO;
import com.msb.pay.model.dto.CashierPayDTO;
import com.msb.pay.model.dto.PrepaymentDTO;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.paydata.PayData;
import com.msb.pay.model.vo.*;
import com.msb.pay.service.PayOrderService;
import com.msb.pay.service.RefundOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "API-支付中心")
@RestController
@RequestMapping("/payCenter")
public class PayCenterController {

    @Resource
    private PayOrderService payOrderService;
    @Resource
    private RefundOrderService refundOrderService;

    @ApiOperation(value = "统一下单")
    @PostMapping("/unifiedOrder")
    public UnifiedOrderVO<? extends PayData> unifiedOrder(@Validated @RequestBody UnifiedOrderDTO unifiedOrderDTO) {
        return payOrderService.unifiedOrder(unifiedOrderDTO);
    }

    @ApiOperation(value = "申请退款")
    @PostMapping("/applyRefund")
    public ApplyRefundVO applyRefund(@Validated @RequestBody ApplyRefundDTO applyRefundDTO) {
        return refundOrderService.applyRefund(applyRefundDTO);
    }

    @ApiOperation(value = "预支付下单")
    @PostMapping("/prepayment")
    public PrepaymentVO prepayment(@Validated @RequestBody PrepaymentDTO prepaymentDTO) {
        return payOrderService.prepayment(prepaymentDTO);
    }

    @ApiOperation(value = "预支付订单信息")
    @GetMapping("/prepayOrder/{payOrderNo}")
    public PrepayOrderVO prepayOrder(@ApiParam("支付订单号") @PathVariable String payOrderNo) {
        return payOrderService.prepayOrder(payOrderNo);
    }

    @ApiOperation(value = "收银台支付")
    @PutMapping("/cashierPay")
    public UnifiedOrderVO<? extends PayData> cashierPay(@Validated @RequestBody CashierPayDTO cashierPayDTO) {
        return payOrderService.cashierPay(cashierPayDTO);
    }

    @Transform
    @ApiOperation(value = "查询支付结果")
    @GetMapping("/payResult/{payOrderNo}")
    public PayResultVO payResult(@ApiParam("支付订单号") @PathVariable String payOrderNo) {
        return payOrderService.payResult(payOrderNo);
    }

}
