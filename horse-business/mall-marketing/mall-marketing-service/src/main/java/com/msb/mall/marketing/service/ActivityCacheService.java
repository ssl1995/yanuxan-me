package com.msb.mall.marketing.service;

import com.msb.framework.redis.RedisClient;
import com.msb.mall.marketing.api.model.ActivityProductDO;
import com.msb.mall.marketing.model.RedisKeysConstants;
import com.msb.mall.marketing.model.entity.ActivityProduct;
import com.msb.mall.marketing.model.vo.ActivityTimeVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.msb.mall.marketing.model.RedisKeysConstants.PRODUCT_ACTIVITY;

@Service
public class ActivityCacheService {

    @Resource
    private RedisClient redisClient;

    @Resource
    private ActivityTimeService activityTimeService;

    @Resource
    private ActivityProductService activityProductService;


    public Boolean getProductIsActivity(Long productId) {
        Boolean flag = redisClient.get(PRODUCT_ACTIVITY.concat(String.valueOf(productId)));
        return Optional.ofNullable(flag).orElseGet(() -> updateProductIsActivityByProduct(productId));
    }

    public void updateProductIsActivityByActivity(Long activityId) {
        List<ActivityTimeVO> activityTimeList = activityTimeService.listActivityTime(activityId);
        activityTimeList.stream().map(ActivityTimeVO::getId).forEach(this::updateProductIsActivityByActivityTime);
    }

    public void updateProductIsActivityByActivityTime(Long activityTimeId) {
        List<ActivityProduct> activityProducts = activityProductService.listActivityProduct(activityTimeId);
        activityProducts.stream().map(ActivityProduct::getProductId).forEach(this::updateProductIsActivityByProduct);
    }

    @Async
    public void updateProductIsActivityByProduct(List<Long> productIdList) {
        productIdList.forEach(this::updateProductIsActivityByProduct);
    }

    public Boolean updateProductIsActivityByProduct(Long productId) {
        ActivityProductDO activityProductDO = activityProductService.getActivityProductDO(productId);
        if (activityProductDO.getIsActivityProduct()) {
            Duration duration = Duration.between(LocalDateTime.now(), activityProductDO.getActivityEndTime());
            redisClient.set(PRODUCT_ACTIVITY.concat(String.valueOf(productId)),
                    activityProductDO.getIsActivityProduct(), duration.getSeconds(), TimeUnit.SECONDS);
        } else {
            redisClient.set(PRODUCT_ACTIVITY.concat(String.valueOf(productId)),
                    activityProductDO.getIsActivityProduct(), 30, TimeUnit.DAYS);
        }
        return activityProductDO.getIsActivityProduct();
    }
}
