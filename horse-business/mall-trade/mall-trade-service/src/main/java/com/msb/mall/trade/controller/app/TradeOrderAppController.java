package com.msb.mall.trade.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kuaidi100.sdk.response.SubscribeResp;
import com.msb.framework.common.context.UserContext;
import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.util.ResponseUtil;
import com.msb.mall.trade.model.dto.app.*;
import com.msb.mall.trade.model.vo.app.*;
import com.msb.mall.trade.service.TradeOrderLogisticsService;
import com.msb.mall.trade.service.TradeOrderService;
import com.msb.mall.trade.third.logistics.LogisticsApiConfigEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 交易订单(TradeOrder)表控制层
 *
 * @author makejava
 * @since 2022-03-24 18:30:16
 */
@Slf4j
@Api(tags = "APP-交易订单")
@RestController
@RequestMapping("/app/tradeOrder")
public class TradeOrderAppController {

    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private TradeOrderLogisticsService tradeOrderLogisticsService;

    //    @RepeatRequestIntercept
    @ApiOperation(value = "提交订单")
    @PostMapping("/submitOrder")
    public OrderSubmitVO submitOrder(@Validated @RequestBody OrderSubmitDTO orderSubmitDTO) {
        log.info("APP提交订单参数：{}", orderSubmitDTO);
        return tradeOrderService.submitOrder(orderSubmitDTO);
    }

    //    @RepeatRequestIntercept
    @ApiOperation(value = "预订单-立即购买")
    @GetMapping("/buyAdvanceOrder")
    public AdvanceOrderVO buyAdvanceOrder(@Validated AdvanceOrderDTO advanceOrderDTO) {
        log.info("APP立即购买预订单参数：{}", advanceOrderDTO);
        return tradeOrderService.advanceOrderForBuyNow(advanceOrderDTO);
    }

    //    @RepeatRequestIntercept
    @ApiOperation(value = "预订单-购物车")
    @GetMapping("/cartAdvanceOrder")
    public AdvanceOrderVO cartAdvanceOrder(@ApiParam("购物车ID数组") Long[] cartIds, @ApiParam("收货地址ID") Long recipientAddressId, @ApiParam("是否为虚拟商品订单") Boolean isVirtual) {
        log.info("APP购物车预订单参数：{}，{}，{}", cartIds, recipientAddressId, isVirtual);
        return tradeOrderService.advanceOrderForCart(cartIds, recipientAddressId, !Objects.isNull(isVirtual) && isVirtual);
    }

    @Transform
    @ApiOperation(value = "订单分页列表")
    @GetMapping("/page")
    public IPage<OrderListAppVO> page(@Validated OrderQueryAppDTO orderQueryAppDTO) {
        log.info("APP订单分页列表参数：{}", orderQueryAppDTO);
        return tradeOrderService.pageOrderByApp(orderQueryAppDTO);
    }

    @Transform
    @ApiOperation(value = "待收货订单物流信息列表")
    @GetMapping("/listReceiveOrder")
    public IPage<OrderLogisticsListVO> listReceiveOrder(@Validated PageDTO pageDTO) {
        return tradeOrderService.listReceiveOrder(pageDTO);
    }

    @Transform
    @ApiOperation(value = "订单详情信息")
    @GetMapping("/{orderId}")
    public OrderInfoAppVO orderInfo(@PathVariable Long orderId) {
        return tradeOrderService.getOrderInfoByApp(orderId);
    }

    @Transform
    @ApiOperation(value = "订单支付结果")
    @GetMapping("/payResult/{orderId}")
    public OrderPayResultVO orderPayResult(@PathVariable Long orderId) {
        return tradeOrderService.orderPayResult(orderId);
    }

    @Transform
    @ApiOperation(value = "订单商品详情信息")
    @GetMapping("/product/{orderProductId}")
    public OrderProductInfoVO orderProductInfo(@PathVariable Long orderProductId) {
        return tradeOrderService.getOrderProductInfo(orderProductId);
    }

    @Transform
    @ApiOperation(value = "查看物流")
    @GetMapping("/logistics/{orderId}")
    public LogisticsInfoAppVO logisticsInfo(@PathVariable Long orderId) {
        return tradeOrderService.logisticsInfoByApp(orderId);
    }

    @ApiOperation(value = "取消订单")
    @PutMapping("/cancel")
    public Boolean cancel(@Validated @RequestBody OrderCancelDTO orderCancelDTO) {
        log.info("APP取消订单参数：{}", orderCancelDTO);
        return tradeOrderService.cancelByUser(orderCancelDTO);
    }

    @ApiOperation(value = "确认收货")
    @PutMapping("/receive")
    public Boolean receive(@Validated @RequestBody OrderReceiveDTO orderReceiveDTO) {
        log.info("APP确认收货参数：{}", orderReceiveDTO);
        return tradeOrderService.receive(orderReceiveDTO);
    }

    @ApiOperation("订单统计")
    @GetMapping("/statistics")
    public OrderStatisticsVO statistics() {
        return tradeOrderService.statisticsByUser(UserContext.getUserId());
    }

    @ApiOperation(value = "刷新物流")
    @GetMapping("/refreshLogistics/{orderId}")
    public LogisticsExpressQueryVO refreshLogistics(@PathVariable Long orderId) {
        return tradeOrderLogisticsService.refreshLogistics(orderId);
    }

    @ApiOperation(value = "快递100订单发货物流订阅通知", hidden = true)
    @RequestMapping("/subscribeExpress100")
    public void subscribeExpress100(HttpServletRequest request, HttpServletResponse response) {
        tradeOrderLogisticsService.subscribeCallback(LogisticsApiConfigEnum.EXPRESS100.getCode(), request.getParameter("param"));
        SubscribeResp subscribeResp = new SubscribeResp();
        subscribeResp.setResult(Boolean.TRUE);
        subscribeResp.setReturnCode("200");
        subscribeResp.setMessage("成功");
        ResponseUtil.writeObject(response, subscribeResp);
    }

    @ApiOperation(value = "待评价订单详情信息列表")
    @GetMapping("/listOrderProductWaitComment")
    public IPage<OrderProductVO> listOrderProductWaitComment(PageDTO pageDTO){
        return tradeOrderService.listOrderProductWaitComment(pageDTO);
    }

    @ApiOperation(value = "根据订单状态查询订单详情信息列表")
    @GetMapping("/listOrderProductByCommentStatus")
    public IPage<OrderProductVO> listOrderProductByCommentStatus(OrderCommentStatusDTO orderCommentStatusDTO) {
        return tradeOrderService.listOrderProductByCommentStatus(orderCommentStatusDTO);
    }


}

