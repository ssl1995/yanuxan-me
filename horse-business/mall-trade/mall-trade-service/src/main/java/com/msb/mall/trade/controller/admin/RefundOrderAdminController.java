package com.msb.mall.trade.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.web.tool.excel.ExportExcel;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.mall.trade.enums.RefundOperationLogTypeEnum;
import com.msb.mall.trade.model.dto.admin.ReceivingAuditDTO;
import com.msb.mall.trade.model.dto.admin.RefundAuditDTO;
import com.msb.mall.trade.model.dto.admin.RefundQueryAdminDTO;
import com.msb.mall.trade.model.dto.admin.ReturnAuditDTO;
import com.msb.mall.trade.model.vo.admin.RefundInfoByOnlyRefundVO;
import com.msb.mall.trade.model.vo.admin.RefundInfoByReturnVO;
import com.msb.mall.trade.model.vo.admin.RefundListAdminVO;
import com.msb.mall.trade.model.vo.admin.RefundStatisticsAdminVO;
import com.msb.mall.trade.model.vo.app.RefundLogisticsVO;
import com.msb.mall.trade.service.RefundOrderService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@AuthAdmin
@Api(tags = "后管-退款订单")
@RestController
@RequestMapping("/admin/refundOrder")
public class RefundOrderAdminController {

    @Resource
    private RefundOrderService refundOrderService;

    @Transform
    @ApiOperation(value = "退款单统计")
    @GetMapping("/statistics")
    public RefundStatisticsAdminVO statistics() {
        return refundOrderService.statisticsByAdmin();
    }

    @Transform
    @ExportExcel(fileName = "售后单导出")
    @ApiOperation("退款单分页列表")
    @GetMapping("/page")
    public IPage<RefundListAdminVO> page(@Validated RefundQueryAdminDTO refundQueryAdminDTO) {
        log.info("后管退款单分页列表参数：{}", refundQueryAdminDTO);
        return refundOrderService.pageRefundByAdmin(refundQueryAdminDTO);
    }

    @Transform
    @ApiOperation("（仅退款）退款单详情信息")
    @GetMapping("/refundInfo/{refundId}")
    public RefundInfoByOnlyRefundVO refundInfo(@PathVariable Long refundId) {
        return refundOrderService.getRefundInfoByAdmin(refundId);
    }

    @Transform
    @ApiOperation("（退货退款）退款单详情信息")
    @GetMapping("/returnInfo/{refundId}")
    public RefundInfoByReturnVO returnInfo(@PathVariable Long refundId) {
        return refundOrderService.getReturnInfoByAdmin(refundId);
    }

    @ApiOperation("（仅退款）同意退款")
    @PutMapping("/agreeRefund")
    public Boolean agreeRefund(@Validated @RequestBody RefundAuditDTO refundAuditDTO) {
        log.info("后管同意退款参数：{}", refundAuditDTO);
        return refundOrderService.agreeRefund(refundAuditDTO, RefundOperationLogTypeEnum.AGREE_REFUND);
    }

    @ApiOperation("（仅退款）拒绝退款")
    @PutMapping("/disagreeRefund")
    public Boolean disagreeRefund(@Validated @RequestBody RefundAuditDTO refundAuditDTO) {
        log.info("后管拒绝退款参数：{}", refundAuditDTO);
        return refundOrderService.disagreeRefund(refundAuditDTO);
    }

    @ApiOperation("（退货退款）同意退货")
    @PutMapping("/agreeReturn")
    public Boolean agreeReturn(@Validated @RequestBody ReturnAuditDTO returnAuditDTO) {
        log.info("后管同意退货参数：{}", returnAuditDTO);
        return refundOrderService.agreeReturn(returnAuditDTO, RefundOperationLogTypeEnum.AGREE_RETURN);
    }

    @ApiOperation("（退货退款）拒绝退货")
    @PutMapping("/disagreeReturn")
    public Boolean disagreeReturn(@Validated @RequestBody ReturnAuditDTO returnAuditDTO) {
        log.info("后管拒绝退货参数：{}", returnAuditDTO);
        return refundOrderService.disagreeReturn(returnAuditDTO);
    }

    @ApiOperation("（退货退款）确认收货")
    @PutMapping("/agreeReceiving")
    public Boolean agreeReceiving(@Validated @RequestBody ReceivingAuditDTO receivingAuditDTO) {
        log.info("后管确认收货参数：{}", receivingAuditDTO);
        return refundOrderService.agreeReceiving(receivingAuditDTO, RefundOperationLogTypeEnum.AGREE_RECEIVING);
    }

    @ApiOperation("（退货退款）拒绝收货")
    @PutMapping("/disagreeReceiving")
    public Boolean disagreeReceiving(@Validated @RequestBody ReceivingAuditDTO receivingAuditDTO) {
        log.info("后管拒绝收货参数：{}", receivingAuditDTO);
        return refundOrderService.disagreeReceiving(receivingAuditDTO);
    }

    @Transform
    @ApiOperation(value = "查看物流")
    @GetMapping("/logistics/{refundId}")
    public RefundLogisticsVO logisticsInfo(@PathVariable Long refundId) {
        return refundOrderService.logisticsInfoByAdmin(refundId);
    }

}
