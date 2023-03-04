package com.msb.pay.controller.api;

import com.msb.pay.kit.SignKit;
import com.msb.pay.model.dto.TestUnifiedOrderDTO;
import com.msb.pay.model.dto.UnifiedOrderDTO;
import com.msb.pay.model.entity.AppInfo;
import com.msb.pay.model.paydata.PayData;
import com.msb.pay.model.vo.UnifiedOrderVO;
import com.msb.pay.service.AppInfoService;
import com.msb.pay.service.PayOrderService;
import com.msb.pay.service.convert.ModelConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = "API-支付测试")
@RestController
@RequestMapping("/payTest")
public class TestController {

    @Resource
    private PayOrderService payOrderService;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private ModelConvert modelConvert;

    @ApiOperation(value = "统一下单", hidden = true)
//    @PostMapping("/unifiedOrder")
    public UnifiedOrderVO<? extends PayData> unifiedOrder(@Validated @RequestBody TestUnifiedOrderDTO testUnifiedOrderDTO) {
        UnifiedOrderDTO unifiedOrderDTO = modelConvert.toUnifiedOrderDTO(testUnifiedOrderDTO);
        AppInfo appInfo = appInfoService.getByCodeOrThrow(testUnifiedOrderDTO.getAppCode());
        SignKit.setSign(unifiedOrderDTO, appInfo.getSignKey());
        return payOrderService.unifiedOrder(unifiedOrderDTO);
    }

}
