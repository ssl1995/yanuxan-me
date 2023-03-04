package com.msb.mall.trade.controller.pay;

import com.msb.framework.web.util.ResponseUtil;
import com.msb.mall.trade.config.PayCenterConfig;
import com.msb.mall.trade.model.dto.pay.AuthPayDTO;
import com.msb.mall.trade.model.dto.pay.BasePayDTO;
import com.msb.mall.trade.model.dto.pay.ReturnPayDTO;
import com.msb.mall.trade.service.RefundOrderService;
import com.msb.mall.trade.service.TradeOrderService;
import com.msb.pay.enums.PayCodeEnum;
import com.msb.pay.enums.PayStatusEnum;
import com.msb.pay.enums.RefundStatusEnum;
import com.msb.pay.kit.SignKit;
import com.msb.pay.model.constant.PayCenterConst;
import com.msb.pay.model.dto.PayNotifyDTO;
import com.msb.pay.model.dto.RefundNotifyDTO;
import com.msb.pay.model.paydata.*;
import com.msb.pay.model.vo.PrepaymentVO;
import com.msb.pay.model.vo.UnifiedOrderVO;
import com.msb.user.auth.NoAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Api(tags = "PAY-支付中台")
@RestController
@RequestMapping("/payCenter")
public class PayCenterController {

    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private PayCenterConfig payCenterConfig;

    @ApiOperation(value = "收银台预支付")
    @PostMapping("/cashierPrepay")
    public PrepaymentVO cashierPrepay(@Validated @RequestBody ReturnPayDTO returnPayDTO) {
        return tradeOrderService.cashierPrepay(returnPayDTO);
    }

    @ApiOperation(value = "微信NATIVE支付")
    @PostMapping("/wxPay/nativeData")
    public UnifiedOrderVO<WxNativeData> wxPayNativeData(@Validated @RequestBody BasePayDTO basePayDTO) {
        return (UnifiedOrderVO<WxNativeData>) tradeOrderService.payRequest(basePayDTO, PayCodeEnum.WX_NATIVE);
    }

    @ApiOperation(value = "微信H5支付")
    @PostMapping("/wxPay/h5")
    public UnifiedOrderVO<WxH5Data> wxPayH5(@Validated @RequestBody BasePayDTO basePayDTO) {
        return (UnifiedOrderVO<WxH5Data>) tradeOrderService.payRequest(basePayDTO, PayCodeEnum.WX_H5);
    }

    @ApiOperation(value = "微信JSAPI支付")
    @PostMapping("/wxPay/jsapi")
    public UnifiedOrderVO<WxJsapiData> wxPayJsapi(@Validated @RequestBody AuthPayDTO authPayDTO) {
        return (UnifiedOrderVO<WxJsapiData>) tradeOrderService.payRequest(authPayDTO, PayCodeEnum.WX_JSAPI);
    }

    @ApiOperation(value = "微信APP支付")
    @PostMapping("/wxPay/app")
    public UnifiedOrderVO<WxAppData> wxPayApp(@Validated @RequestBody BasePayDTO basePayDTO) {
        return (UnifiedOrderVO<WxAppData>) tradeOrderService.payRequest(basePayDTO, PayCodeEnum.WX_APP);
    }

    @ApiOperation(value = "支付宝APP支付")
    @PostMapping("/aliPay/app")
    public UnifiedOrderVO<AliAppData> aliPayApp(@Validated @RequestBody BasePayDTO basePayDTO) {
        return (UnifiedOrderVO<AliAppData>) tradeOrderService.payRequest(basePayDTO, PayCodeEnum.ALI_APP);
    }

    @ApiOperation(value = "支付宝网页支付")
    @PostMapping("/aliPay/wap")
    public UnifiedOrderVO<AliWapData> aliPayWap(@Validated @RequestBody ReturnPayDTO returnPayDTO) {
        return (UnifiedOrderVO<AliWapData>) tradeOrderService.payRequest(returnPayDTO, PayCodeEnum.ALI_WAP);
    }

    @ApiOperation(value = "支付宝PC支付")
    @PostMapping("/aliPay/pc")
    public UnifiedOrderVO<AliPcData> aliPayPc(@Validated @RequestBody BasePayDTO basePayDTO) {
        return (UnifiedOrderVO<AliPcData>) tradeOrderService.payRequest(basePayDTO, PayCodeEnum.ALI_PC);
    }

    @ApiOperation(value = "支付宝扫码支付")
    @PostMapping("/aliPay/qr")
    public UnifiedOrderVO<AliQrData> aliPayQr(@Validated @RequestBody BasePayDTO basePayDTO) {
        return (UnifiedOrderVO<AliQrData>) tradeOrderService.payRequest(basePayDTO, PayCodeEnum.ALI_QR);
    }

    @NoAuth
    @ApiOperation(value = "支付成功回调", hidden = true)
    @PostMapping("/payNotify")
    public void payNotify(PayNotifyDTO payNotifyDTO, HttpServletResponse response) {
        log.info("接收到支付成功的接口消息通知：{}", payNotifyDTO);
        PayCenterConfig.PayApp payApp = payCenterConfig.getByPayCode(payNotifyDTO.getPayCode());
        SignKit.signatureValidate(payNotifyDTO, payApp.getSignKey());
        if (Objects.equals(payNotifyDTO.getPayStatus(), PayStatusEnum.PAY_SUCCESS.getCode())) {
            tradeOrderService.paySuccessCallback(payNotifyDTO);
        }
        ResponseUtil.writeJson(response, PayCenterConst.NOTIFY_RESPONSE.SUCCESS);
    }

    @NoAuth
    @ApiOperation(value = "退款成功回调", hidden = true)
    @PostMapping("/refundNotify")
    public void refundNotify(RefundNotifyDTO refundNotifyDTO, HttpServletResponse response) {
        log.info("接收到退款成功的接口消息通知：{}", refundNotifyDTO);
        PayCenterConfig.PayApp payApp = payCenterConfig.getByAppCode(refundNotifyDTO.getAppCode());
        SignKit.signatureValidate(refundNotifyDTO, payApp.getSignKey());
        if (Objects.equals(refundNotifyDTO.getRefundStatus(), RefundStatusEnum.REFUND_SUCCESS.getCode())) {
            refundOrderService.refundSuccessCallback(refundNotifyDTO.getRefundOrderNo());
        }
        ResponseUtil.writeJson(response, PayCenterConst.NOTIFY_RESPONSE.SUCCESS);
    }

}
