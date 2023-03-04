package com.msb.pay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.pay.model.dto.PayOrderPageDTO;
import com.msb.pay.model.vo.PayOrderPageVO;
import com.msb.pay.model.vo.PayOrderVO;
import com.msb.pay.service.PayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "交易订单")
@RestController
@RequestMapping("/payOrder")
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;

    @Transform
    @ApiOperation(value = "支付订单列表")
    @GetMapping("/page")
    public Page<PayOrderPageVO> page(@Validated PayOrderPageDTO payOrderPageDTO) {
        return payOrderService.page(payOrderPageDTO);
    }

    @Transform
    @ApiOperation(value = "交易订单详情")
    @GetMapping("/{payOrderId}")
    public PayOrderVO tradeQuery(@PathVariable Long payOrderId) {
        return payOrderService.tradeQuery(payOrderId);
    }

}
