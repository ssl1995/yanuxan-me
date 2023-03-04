package com.msb.pay.controller.api;

import com.ijpay.alipay.AliPayApi;
import com.msb.framework.web.util.ResponseUtil;
import com.msb.pay.mq.model.PayOrderDelayNotifyMessage;
import com.msb.pay.mq.model.RefundOrderDelayNotifyMessage;
import com.msb.pay.service.NotifyService;
import com.msb.pay.service.PayOrderService;
import com.msb.pay.service.RefundOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "API-回调通知")
@RestController
@RequestMapping("/notify")
public class NotifyController {

    @Resource
    private PayOrderService payOrderService;
    @Resource
    private RefundOrderService refundOrderService;
    @Lazy
    @Resource
    private NotifyService notifyService;

    @ApiOperation(value = "微信支付通知", hidden = true)
    @PostMapping("/wxPayNotify/{payOrderNo}")
    public void wxPayNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable String payOrderNo) {
        log.info("接收微信支付通知：{}", payOrderNo);
        Map<String, String> responseMap = new HashMap<>(2);
        try {
            payOrderService.payNotify(request, payOrderNo);
            response.setStatus(200);
            responseMap.put("code", "SUCCESS");
            responseMap.put("message", "SUCCESS");
        } catch (Exception e) {
            response.setStatus(500);
            responseMap.put("code", "ERROR");
            responseMap.put("message", "处理失败");
            log.error("微信支付回调处理失败", e);
        }
        log.info("微信支付通知响应：{}", responseMap);
        ResponseUtil.writeObject(response, responseMap);
    }

    @ApiOperation(value = "微信退款通知", hidden = true)
    @PostMapping("/wxRefundNotify/{refundOrderNo}")
    public void wxRefundNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable String refundOrderNo) {
        log.info("接收微信退款通知：{}", refundOrderNo);
        Map<String, String> responseMap = new HashMap<>(2);
        try {
            refundOrderService.refundNotify(request, refundOrderNo);
            response.setStatus(200);
            responseMap.put("code", "SUCCESS");
            responseMap.put("message", "SUCCESS");
        } catch (Exception e) {
            response.setStatus(500);
            responseMap.put("code", "ERROR");
            responseMap.put("message", "处理失败");
            log.error("微信支付回调处理失败", e);
        }
        log.info("微信支付通知响应：{}", responseMap);
        ResponseUtil.writeObject(response, responseMap);
    }

    @ApiOperation(value = "支付宝支付通知", hidden = true)
    @PostMapping("/aliPayNotify/{payOrderNo}")
    public void aliPayNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable String payOrderNo) {
        log.info("接收支付宝支付通知：{}", payOrderNo);
        Map<String, String> params = AliPayApi.toMap(request);
        log.info("接收支付宝支付通知参数：{}", params);
        try {
            payOrderService.payNotify(request, payOrderNo);
            ResponseUtil.writeJson(response, "success");
        } catch (Exception e) {
            log.error("支付宝支付回调处理失败", e);
            ResponseUtil.writeJson(response, "failure");
        }
    }

    @ApiOperation(value = "支付宝退款通知", hidden = true)
    @PostMapping("/aliRefundNotify/{refundOrderNo}")
    public void aliRefundNotify(HttpServletRequest request, HttpServletResponse response, @PathVariable String refundOrderNo) {
        Map<String, String> params = AliPayApi.toMap(request);
        log.info("接收支付宝退款通知：{}", refundOrderNo);
        log.info("接收支付宝退款通知参数：{}", params);
    }

    @ApiOperation(value = "支付订单延时通知")
    @PutMapping("/payOrderDelayNotify/{payOrderId}")
    public Boolean payOrderDelayNotify(@PathVariable Long payOrderId) {
        PayOrderDelayNotifyMessage payOrderDelayNotifyMessage = new PayOrderDelayNotifyMessage().setPayOrderId(payOrderId);
        notifyService.payOrderNotify(payOrderDelayNotifyMessage);
        return true;
    }

    @ApiOperation(value = "退款订单延时通知")
    @PutMapping("/refundOrderDelayNotify/{refundOrderId}")
    public Boolean refundOrderDelayNotify(@PathVariable Long refundOrderId) {
        RefundOrderDelayNotifyMessage refundOrderDelayNotifyMessage = new RefundOrderDelayNotifyMessage().setRefundOrderId(refundOrderId);
        notifyService.refundOrderNotify(refundOrderDelayNotifyMessage);
        return true;
    }

}
