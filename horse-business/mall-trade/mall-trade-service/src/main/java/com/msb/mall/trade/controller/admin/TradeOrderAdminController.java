package com.msb.mall.trade.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.msb.framework.web.tool.excel.ExportExcel;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.mall.trade.enums.LogisticsCompanyEnum;
import com.msb.mall.trade.model.dto.admin.*;
import com.msb.mall.trade.model.vo.admin.*;
import com.msb.mall.trade.model.vo.app.OrderLogisticsVO;
import com.msb.mall.trade.model.vo.app.OrderStatisticsVO;
import com.msb.mall.trade.service.TradeOrderService;
import com.msb.user.auth.AuthAdmin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易订单(TradeOrder)表控制层
 *
 * @author makejava
 * @since 2022-03-24 18:30:16
 */
@Slf4j
@AuthAdmin
@Api(tags = "后管-交易订单")
@RestController
@RequestMapping("admin/tradeOrder")
public class TradeOrderAdminController {

    @Resource
    private TradeOrderService tradeOrderService;

    @ApiOperation("订单统计")
    @GetMapping("/statistics")
    public OrderStatisticsVO statistics() {
        return tradeOrderService.statisticsAll();
    }

    @Transform
    @ExportExcel(fileName = "订单导出")
    @ApiOperation("订单分页列表")
    @GetMapping("/page")
    public IPage<OrderListAdminVO> page(@Validated OrderQueryAdminDTO orderQueryAdminDTO) {
        log.info("后管订单分页列表参数：{}", orderQueryAdminDTO);
        return tradeOrderService.pageOrderByAdmin(orderQueryAdminDTO);
    }

    @Transform
    @ApiOperation("订单详情信息")
    @GetMapping("/{orderId}")
    public OrderInfoAdminVO orderInfo(@PathVariable Long orderId) {
        return tradeOrderService.getOrderInfoByAdmin(orderId);
    }

    @ApiOperation("修改收货人信息")
    @PutMapping("/updateRecipient")
    public Boolean updateRecipient(@Validated @RequestBody OrderRecipientModifyDTO orderRecipientModifyDTO) {
        log.info("后管修改收货人信息参数：{}", orderRecipientModifyDTO);
        return tradeOrderService.updateRecipient(orderRecipientModifyDTO);
    }

    @ApiOperation("修改费用")
    @PutMapping("/updateAmount")
    public Boolean updateAmount(@Validated @RequestBody OrderAmountModifyDTO orderAmountModifyDTO) {
        log.info("后管修改费用参数：{}", orderAmountModifyDTO);
        return tradeOrderService.updateAmount(orderAmountModifyDTO);
    }

    @ApiOperation("关闭订单")
    @PutMapping("/close")
    public Boolean close(@Validated @RequestBody OrderCloseDTO orderCloseDTO) {
        log.info("后管关闭订单参数：{}", orderCloseDTO);
        return tradeOrderService.closeByAdmin(orderCloseDTO);
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    public Boolean cancel(@Validated @RequestBody OrderCancelAdminDTO orderCancelAdminDTO) {
        log.info("后管取消订单参数：{}", orderCancelAdminDTO);
        return tradeOrderService.cancelByAdmin(orderCancelAdminDTO);
    }

    @Transform
    @ApiOperation("查询发货列表订单")
    @GetMapping("/listDeliveryOrder")
    public List<OrderDeliveryVO> listDeliveryOrder(@NotNull Long[] orderIds) {
        return tradeOrderService.listDeliveryOrder(orderIds);
    }

    @ApiOperation("查询物流公司列表")
    @GetMapping("/logisticsCompany")
    public List<LogisticsCompanyVO> logisticsCompany() {
        return Arrays.stream(LogisticsCompanyEnum.values()).map(companyEnum -> {
            return new LogisticsCompanyVO(companyEnum.getCode(), companyEnum.getText());
        }).collect(Collectors.toList());
    }

    @ApiOperation("单笔订单发货")
    @PutMapping("/delivery")
    public Boolean delivery(@Validated @RequestBody OrderDeliveryDTO orderDeliveryDTO) {
        log.info("后管发货参数：{}", orderDeliveryDTO);
        return tradeOrderService.delivery(orderDeliveryDTO);
    }

    @ApiOperation("批量订单发货")
    @PutMapping("/batchDelivery")
    public List<OrderDeliveryResultVO> batchDelivery(@Validated @RequestBody List<OrderDeliveryDTO> deliveryDTOList) {
        log.info("后管批量发货参数：{}", deliveryDTOList);
        return tradeOrderService.batchDelivery(deliveryDTOList);
    }

    @ApiOperation("虚拟订单发货")
    @PutMapping("/virtualDelivery{orderId}")
    public Boolean virtualDelivery(@PathVariable Long orderId) {
        log.info("虚拟订单发货：{}", orderId);
        return tradeOrderService.virtualDelivery(orderId);
    }

    @ApiOperation("批量虚拟订单发货")
    @PutMapping("/batchVirtualDelivery")
    public List<OrderDeliveryResultVO> batchVirtualDelivery(@Validated @RequestBody VirtualDeliveryDTO virtualDeliveryDTO) {
        log.info("批量虚拟订单发货：{}", virtualDeliveryDTO);
        return tradeOrderService.batchVirtualDelivery(virtualDeliveryDTO);
    }

    @Transform
    @ApiOperation(value = "查看物流")
    @GetMapping("/logistics/{orderId}")
    public OrderLogisticsVO logisticsInfo(@PathVariable Long orderId) {
        return tradeOrderService.logisticsInfoByAdmin(orderId);
    }

}

