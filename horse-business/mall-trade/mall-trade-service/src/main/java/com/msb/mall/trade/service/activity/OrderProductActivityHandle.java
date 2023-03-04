package com.msb.mall.trade.service.activity;

import com.msb.mall.product.api.model.ProductSkuDO;
import com.msb.mall.trade.enums.OrderTypeEnum;
import com.msb.mall.trade.model.dto.app.OrderSubmitProductDTO;
import com.msb.mall.trade.model.entity.TradeOrderProduct;

import javax.annotation.Nonnull;

/**
 * 订单商品活动类型处理接口
 */
public interface OrderProductActivityHandle {

    /**
     * 提交订单商品参数，获取商品订单对象
     *
     * @param productDTO：订单商品提交参数
     * @param productSku：商品sku
     * @param isReduceStock：是否扣减库存
     * @return com.msb.mall.trade.model.entity.TradeOrderProduct
     * @author peng.xy
     * @date 2022/4/18
     */
    TradeOrderProduct createOrderProduct(@Nonnull OrderSubmitProductDTO productDTO, @Nonnull ProductSkuDO productSku, boolean isReduceStock);

    /**
     * 获取订单对应的订单类型
     *
     * @return com.msb.mall.trade.enums.OrderTypeEnum
     * @author peng.xy
     * @date 2022/4/22
     */
    OrderTypeEnum getOrderType();

}
