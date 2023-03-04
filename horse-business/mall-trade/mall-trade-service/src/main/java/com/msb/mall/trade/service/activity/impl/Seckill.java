package com.msb.mall.trade.service.activity.impl;

import com.msb.framework.common.exception.BizException;
import com.msb.framework.common.utils.EqualsUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.marketing.api.ActivityDubboService;
import com.msb.mall.marketing.api.model.ActivityProductSkuDO;
import com.msb.mall.marketing.api.model.ActivityProductSkuDTO;
import com.msb.mall.product.api.model.ProductSkuDO;
import com.msb.mall.trade.enums.ActivityTypeEnum;
import com.msb.mall.trade.enums.OrderProductDetailEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import com.msb.mall.trade.model.dto.app.OrderSubmitProductDTO;
import com.msb.mall.trade.model.entity.TradeOrderProduct;
import com.msb.mall.trade.service.activity.OrderProductActivityHandle;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Collections;

/**
 * 秒杀
 *
 * @author peng.xy
 * @date 2022/4/18
 */
@Slf4j
@Service
public class Seckill implements OrderProductActivityHandle {

    @DubboReference
    private ActivityDubboService activityDubboService;

    @Override
    public TradeOrderProduct createOrderProduct(@Nonnull OrderSubmitProductDTO productDTO, @Nonnull ProductSkuDO productSku, boolean isReduceStock) {
        if (EqualsUtil.isAnyNull(productDTO.getActivityId(), productDTO.getActivityTimeId(), productSku.getProductId())) {
            String message = "[".concat(productSku.getProductName()).concat("]秒杀活动参数错误");
            throw new BizException(message);
        }
        ActivityProductSkuDO activityProductSku = activityDubboService.getActivityProductSku(productDTO.getActivityId(), productDTO.getActivityTimeId(), productSku.getProductId(), productSku.getSkuId());
        log.info("activityProductSku {}", activityProductSku);
        BizAssert.notNull(activityProductSku, productSku.getProductName().concat("秒杀活动信息有误"));
        // 组装订单商品信息
        BigDecimal originalPrice = activityProductSku.getOriginalPrice();       // 商品原价
        BigDecimal activityPrice = activityProductSku.getPrice();               // 活动价格
        BigDecimal quantityDecimal = new BigDecimal(productDTO.getQuantity());  // 商品数量
        TradeOrderProduct orderProduct = new TradeOrderProduct()
                .setProductId(productSku.getProductId())                        // 商品ID
                .setProductSkuId(productSku.getSkuId())                         // 商品SKU-ID
                .setProductName(productSku.getProductName())                    // 商品名称
                .setProductImageUrl(productSku.getProductPicture())             // 商品图片
                .setSkuDescribe(productSku.getSkuName())                        // 商品规格
                .setQuantity(productDTO.getQuantity())                          // 商品数量
                .setProductPrice(originalPrice)                                 // 商品原价
                .setRealPrice(activityPrice)                                    // 实际价格
                .setRealAmount(activityPrice.multiply(quantityDecimal))         // 商品金额
                .setActivityId(activityProductSku.getId())                      // 秒杀活动商品ID
                .setActivityType(ActivityTypeEnum.SECKILL.getCode())            // 秒杀
                .setDetailStatus(OrderProductDetailEnum.NORMAL.getCode());
        if (isReduceStock) {
            // 扣减商品sku秒杀库存
            ActivityProductSkuDTO activityProductSkuDTO = new ActivityProductSkuDTO().setActivityProductSkuId(activityProductSku.getId())
                    .setQuantity(productDTO.getQuantity()).setProductName(productSku.getProductName()).setSkuName(productSku.getSkuName());
            activityDubboService.checkAndReduceActivityStock(Collections.singletonList(activityProductSkuDTO));
        }
        log.info("秒杀商品[{}][{}]", orderProduct);
        return orderProduct;
    }

    @Override
    public OrderTypeEnum getOrderType() {
        return OrderTypeEnum.SECKILL;
    }

}
