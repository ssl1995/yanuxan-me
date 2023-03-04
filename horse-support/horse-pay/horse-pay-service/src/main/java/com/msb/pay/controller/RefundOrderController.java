package com.msb.pay.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.pay.model.dto.RefundOrderPageDTO;
import com.msb.pay.model.vo.RefundOrderPageVO;
import com.msb.pay.model.vo.RefundOrderVO;
import com.msb.pay.service.RefundOrderService;
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
@Api(tags = "退款订单")
@RestController
@RequestMapping("/refundOrder")
public class RefundOrderController {

    @Resource
    private RefundOrderService refundOrderService;

    @Transform
    @ApiOperation(value = "退款订单列表")
    @GetMapping("/page")
    public Page<RefundOrderPageVO> page(@Validated RefundOrderPageDTO refundOrderPageDTO) {
        return refundOrderService.page(refundOrderPageDTO);
    }

    @Transform
    @ApiOperation(value = "退款订单详情")
    @GetMapping("/{refundOrderId}")
    public RefundOrderVO refundQuery(@PathVariable Long refundOrderId) {
        return refundOrderService.refundQuery(refundOrderId);
    }

}
