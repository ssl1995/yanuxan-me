package com.msb.mall.trade.api.dubbo;


import com.msb.mall.trade.api.model.TradeOrderProductDO;

import java.util.List;


public interface TradeOrderProductDubboService {
    /**
     * 根据商品id查询订单详情信息
     * @param productId
     * @return
     */
    List<TradeOrderProductDO> listTradeOrderByProductId(Long productId);

    /**
     * 根据商品id查询订单详情信息
     * @param productIdList
     * @return
     */
    List<TradeOrderProductDO> listTradeOrderByProductId(List<Long> productIdList);

    /**
     * 根据id查询订单详情
     * @param orderProductId 订单详情id
     * @return 订单详情
     */
    TradeOrderProductDO getTradeOrderProduct(Long orderProductId);

    /**
     * 根据订单详情id查询订单详情
     * @param orderProductIdList 订单详情id
     * @return 订单详情
     */
    List<TradeOrderProductDO> listTradeOrderProduct(List<Long> orderProductIdList);

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    List<TradeOrderProductDO> listTradeOrderProductByOrderId(Long orderId);

    /**
     * 更新订单详情状态
     * @param commentStatus 订单详情状态
     * @return 更新结果
     */
    Boolean updateTradeOrderProductCommentStatus(Long orderProductId, Integer commentStatus);
}
