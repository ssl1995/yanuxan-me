package com.msb.mall.marketing.manager.label;

import com.msb.mall.marketing.api.model.ActivityProductDO;
import com.msb.mall.marketing.service.ActivityCacheService;
import com.msb.mall.marketing.service.ActivityProductService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("second_kill")
public class ProductActivityLabel implements ProductLabel {


    @Resource
    private ActivityProductService activityProductService;

    @Resource
    private ActivityCacheService activityCacheService;

    @Override
    public Boolean checkLabel(Long productId) {
        return activityCacheService.getProductIsActivity(productId);
    }
}
