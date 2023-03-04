package com.msb.mall.trade.service.activity.impl;

import com.msb.mall.product.api.model.ProductSkuDO;
import com.msb.mall.trade.enums.ActivityTypeEnum;
import com.msb.mall.trade.enums.OrderProductDetailEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import com.msb.mall.trade.model.dto.app.OrderSubmitProductDTO;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import com.msb.mall.trade.service.activity.OrderProductActivityHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

/**
 * 正常购买
 *
 * @author peng.xy
 * @date 2022/4/18
 */
@Slf4j
@Service
public class Buy implements OrderProductActivityHandle {

    @Override
    public TradeOrderProduct createOrderProduct(@Nonnull OrderSubmitProductDTO productDTO, @Nonnull ProductSkuDO productSku, boolean isReduceStock) {
        BigDecimal sellPrice = productSku.getSellPrice();                       // 商品价格
        BigDecimal quantityDecimal = new BigDecimal(productDTO.getQuantity());  // 商品数量
        // 组装订单商品信息
        TradeOrderProduct orderProduct = new TradeOrderProduct()
                .setProductId(productSku.getProductId())                        // 商品ID
                .setProductSkuId(productSku.getSkuId())                         // 商品SKU-ID
                .setProductName(productSku.getProductName())                    // 商品名称
                .setProductImageUrl(productSku.getProductPicture())             // 商品图片
                .setSkuDescribe(productSku.getSkuName())                        // 商品规格
                .setQuantity(productDTO.getQuantity())                          // 商品数量
                .setProductPrice(sellPrice)                                     // 商品原价
                .setRealPrice(sellPrice)                                        // 实际价格
                .setRealAmount(sellPrice.multiply(quantityDecimal))             // 商品金额
                .setActivityType(ActivityTypeEnum.NORMAL.getCode())             // 正常购买
                .setDetailStatus(OrderProductDetailEnum.NORMAL.getCode());
        log.info("正常购买[{}][{}]", orderProduct);
        return orderProduct;
    }

    @Override
    public OrderTypeEnum getOrderType() {
        return OrderTypeEnum.NORMAL;
    }

}
