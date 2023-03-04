package com.msb.mall.trade.service;

import com.msb.framework.common.constant.CommonConst;
import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.web.result.Assert;
import com.msb.mall.base.api.model.ReceiveAddressDO;
import com.msb.mall.product.api.dubbo.ShoppingCartDubboService;
import com.msb.mall.trade.enums.OrderOperationLogTypeEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import com.msb.mall.trade.exception.TradeExceptionCodeEnum;
import com.msb.mall.trade.model.entity.TradeOrder;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 交易订单异步服务实现类
 *
 * @author makejava
 * @since 2022-03-24 18:30:16
 */
@Slf4j
@Async
@Service("asyncService")
public class AsyncService {

    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private TradeOrderProductService tradeOrderProductService;
    @Resource
    private TradeOrderLogisticsService tradeOrderLogisticsService;
    @Resource
    private TradeOrderLogService tradeOrderLogService;
    @DubboReference
    private ShoppingCartDubboService shoppingCartDubboService;

    /**
     * 提交订单异步保存信息
     *
     * @param tradeOrder：交易订单信息
     * @param orderProductList：订单商品信息
     * @param receiveAddressDO：收货地址信息
     * @param shoppingCartIds：购物车ID
     * @author peng.xy
     * @date 2022/5/23
     */
    @Transactional(rollbackFor = Exception.class)
    public void submitOrderAsync(TradeOrder tradeOrder, List<TradeOrderProduct> orderProductList, ReceiveAddressDO receiveAddressDO, List<Long> shoppingCartIds) {
        Long orderId = tradeOrder.getId();
        Long userId = tradeOrder.getUserId();
        // 保存订单商品信息，补全订单ID，用户ID
        for (TradeOrderProduct orderProduct : orderProductList) {
            orderProduct.setOrderId(orderId);
            orderProduct.setUserId(userId);
        }
        boolean saveProducts = tradeOrderProductService.saveBatch(orderProductList);
        Assert.isTrue(saveProducts, TradeExceptionCodeEnum.ORDER_PRODUCT_SAVE_FAIL);
        // 创建订单物流信息
        tradeOrderLogisticsService.saveOrderLogistics(orderId, receiveAddressDO);
        // 删除购物车
        if (CollectionUtils.isNotEmpty(shoppingCartIds)) {
            shoppingCartDubboService.removeShoppingCartByIds(shoppingCartIds);
        }
        // 记录提交订单日志
        tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.SUBMIT_ORDER);
        // 免费订单记录支付日志
        if (EqualsUtil.anyEqualsIDict(tradeOrder.getOrderType(), OrderTypeEnum.FREE)) {
            tradeOrderLogService.saveOrderLogs(orderId, OrderOperationLogTypeEnum.PAY_ORDER, "免费订单，无需支付");
        }
        tradeOrderService.updateById(new TradeOrder().setId(orderId).setIsDisabled(CommonConst.NO));
        log.info("异步保存订单信息成功，订单ID：{}", orderId);
    }

}