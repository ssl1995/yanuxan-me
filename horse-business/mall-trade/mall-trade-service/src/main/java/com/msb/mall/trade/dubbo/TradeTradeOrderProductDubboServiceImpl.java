package com.msb.mall.trade.dubbo;

import com.msb.mall.trade.api.dubbo.TradeOrderProductDubboService;
import com.msb.mall.trade.api.model.TradeOrderProductDO;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import com.msb.mall.trade.service.TradeOrderProductService;
import com.msb.mall.trade.service.convert.TradeOrderProductConvert;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author shumengjiao
 */
@DubboService
public class TradeTradeOrderProductDubboServiceImpl implements TradeOrderProductDubboService {
    @Resource
    private TradeOrderProductService tradeOrderProductService;
    @Resource
    private TradeOrderProductConvert tradeOrderProductConvert;

    @Override
    public List<TradeOrderProductDO> listTradeOrderByProductId(Long productId) {
        return tradeOrderProductService.listTradeOrderByProductId(productId);
    }

    @Override
    public List<TradeOrderProductDO> listTradeOrderByProductId(List<Long> productIdList) {
        return tradeOrderProductService.listTradeOrderByProductId(productIdList);
    }

    @Override
    public TradeOrderProductDO getTradeOrderProduct(Long orderProductId) {
        TradeOrderProduct byId = tradeOrderProductService.getById(orderProductId);
        return tradeOrderProductConvert.toTradeOrderProductDO(byId);
    }

    @Override
    public List<TradeOrderProductDO> listTradeOrderProduct(List<Long> orderProductIdList) {
        List<TradeOrderProduct> tradeOrderProducts = tradeOrderProductService.listByIds(orderProductIdList);
        if (CollectionUtils.isEmpty(tradeOrderProducts)) {
            return Collections.emptyList();
        }
        return tradeOrderProductConvert.toTradeOrderProductDO(tradeOrderProducts);
    }

    @Override
    public List<TradeOrderProductDO> listTradeOrderProductByOrderId(Long orderId) {
        List<TradeOrderProduct> tradeOrderProducts = tradeOrderProductService.listByOrderIds(orderId);
        return tradeOrderProductConvert.toTradeOrderProductDO(tradeOrderProducts);
    }

    @Override
    public Boolean updateTradeOrderProductCommentStatus(Long orderProductId, Integer commentStatus) {
        return tradeOrderProductService.updateCommentStatusByOrderProductId(orderProductId, commentStatus);
    }
}
