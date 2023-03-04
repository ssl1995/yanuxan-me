package com.msb.mall.marketing.manager.label;

import com.msb.mall.marketing.service.ProductRecommendedService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("recommended")
public class ProductRecommendedLabel implements ProductLabel {

    @Resource
    private ProductRecommendedService productRecommendedService;

    @Override
    public Boolean checkLabel(Long productId) {
        return productRecommendedService.getProductIsRecommended(productId);
    }
}
