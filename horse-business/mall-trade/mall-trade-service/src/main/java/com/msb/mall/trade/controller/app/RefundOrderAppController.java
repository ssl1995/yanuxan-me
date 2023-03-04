package com.msb.mall.trade.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kuaidi100.sdk.response.SubscribeResp;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.util.ResponseUtil;
import com.msb.mall.trade.enums.LogisticsCompanyEnum;
import com.msb.mall.trade.model.dto.app.*;
import com.msb.mall.trade.model.entity.RefundOrder;
import com.msb.mall.trade.model.vo.admin.LogisticsCompanyVO;
import com.msb.mall.trade.model.vo.app.RefundApplyVO;
import com.msb.mall.trade.model.vo.app.RefundInfoAppVO;
import com.msb.mall.trade.model.vo.app.RefundListAppVO;
import com.msb.mall.trade.model.vo.app.RefundStatisticsAppVO;
import com.msb.mall.trade.service.RefundOrderLogisticsService;
import com.msb.mall.trade.service.RefundOrderService;
import com.msb.mall.trade.third.logistics.LogisticsApiConfigEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "APP-退款订单")
@RestController
@RequestMapping("/app/refundOrder")
public class RefundOrderAppController {

    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private RefundOrderLogisticsService refundOrderLogisticsService;

//    @RepeatRequestIntercept
    @ApiOperation(value = "申请退款")
    @PostMapping("/applyRefund")
    public RefundApplyVO applyRefund(@Validated @RequestBody RefundApplyDTO refundApplyDTO) {
        log.info("APP申请退款参数：{}", refundApplyDTO);
        RefundOrder refundOrder = refundOrderService.applyRefund(refundApplyDTO);
        return new RefundApplyVO(refundOrder.getId());
    }

    @Transform
    @ApiOperation("退款单分页列表")
    @GetMapping("/page")
    public IPage<RefundListAppVO> page(@Validated PageDTO pageDTO) {
        log.info("APP退款单分页列表参数：{}", pageDTO);
        return refundOrderService.pageRefundByApp(pageDTO);
    }

    @Transform
    @ApiOperation("退款单详情")
    @GetMapping("/{refundId}")
    public RefundInfoAppVO refundInfo(@PathVariable Long refundId) {
        return refundOrderService.getRefundInfoByApp(refundId);
    }

    @Transform
    @ApiOperation("退款单详情（多参数）")
    @GetMapping("/refundInfo")
    public RefundInfoAppVO refundInfo(@Validated RefundInfoDTO refundInfoDTO) {
        return refundOrderService.getRefundInfoByApp(refundInfoDTO);
    }

    @ApiOperation("修改退款申请")
    @PutMapping("/updateRefund")
    public Boolean updateRefund(@Validated @RequestBody RefundModifyDTO refundModifyDTO) {
        log.info("APP修改退款申请参数：{}", refundModifyDTO);
        return refundOrderService.updateRefund(refundModifyDTO);
    }

    @ApiOperation("撤销退款申请")
    @PutMapping("/cancelRefund")
    public Boolean cancelRefund(@Validated @RequestBody RefundCancelDTO refundCancelDTO) {
        log.info("APP撤销退款申请参数：{}", refundCancelDTO);
        return refundOrderService.cancelRefund(refundCancelDTO);
    }

    @ApiOperation("填写退货信息")
    @PutMapping("completeRefund")
    public Boolean completeRefund(@Validated @RequestBody RefundCompleteDTO refundCompleteDTO) {
        log.info("APP填写退货信息参数：{}", refundCompleteDTO);
        return refundOrderService.completeRefund(refundCompleteDTO);
    }

    @ApiOperation("查询物流公司列表")
    @GetMapping("/logisticsCompany")
    public List<LogisticsCompanyVO> logisticsCompany() {
        return Arrays.stream(LogisticsCompanyEnum.values()).map(companyEnum -> {
            return new LogisticsCompanyVO(companyEnum.getCode(), companyEnum.getText());
        }).collect(Collectors.toList());
    }

    @ApiOperation("退款单统计")
    @GetMapping("/statistics")
    public RefundStatisticsAppVO statistics() {
        return refundOrderService.statisticsByUser(UserContext.getUserId());
    }

    @ApiOperation(value = "快递100退款单退货物流订阅通知", hidden = true)
    @RequestMapping("/subscribeExpress100")
    public void subscribeExpress100(HttpServletRequest request, HttpServletResponse response) {
        refundOrderLogisticsService.subscribeCallback(LogisticsApiConfigEnum.EXPRESS100.getCode(), request.getParameter("param"));
        SubscribeResp subscribeResp = new SubscribeResp();
        subscribeResp.setResult(Boolean.TRUE);
        subscribeResp.setReturnCode("200");
        subscribeResp.setMessage("成功");
        ResponseUtil.writeObject(response, subscribeResp);
    }

}
