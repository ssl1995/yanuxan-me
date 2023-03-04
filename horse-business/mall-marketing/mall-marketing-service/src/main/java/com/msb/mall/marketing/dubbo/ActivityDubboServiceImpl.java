package com.msb.mall.marketing.dubbo;

import com.msb.mall.marketing.api.ActivityDubboService;
import com.msb.mall.marketing.api.model.ActivityProductDO;
import com.msb.mall.marketing.api.model.ActivityProductSkuDO;
import com.msb.mall.marketing.api.model.ActivityProductSkuDTO;
import com.msb.mall.marketing.api.model.ActivityProductSkuReturnStockDTO;
import com.msb.mall.marketing.service.ActivityProductService;
import com.msb.mall.marketing.service.ActivityProductSkuService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import javax.annotation.Resource;
import java.util.List;

@DubboService(methods = {
        @Method(name = "getActivityProductDO", timeout = 10000, retries = 1),
        @Method(name = "getActivityProductSku", timeout = 10000, retries = 1)})
public class ActivityDubboServiceImpl implements ActivityDubboService {

    @Resource
    private ActivityProductSkuService activityProductSkuService;

    @Resource
    private ActivityProductService activityProductService;

    @Override
    public void checkAndReduceActivityStock(List<ActivityProductSkuDTO> activityProductSkuListDTO) {
        activityProductSkuService.checkAndReduceActivityStock(activityProductSkuListDTO);
    }

    @Override
    public ActivityProductDO getActivityProductDO(Long productId) {
        return activityProductService.getActivityProductDO(productId);
    }


    @Override
    public ActivityProductSkuDO getActivityProductSku(Long activityId, Long activityTimeId, Long productId, Long skuId) {
        return activityProductService.getActivityProductSku(activityId, activityTimeId, productId, skuId);
    }

    @Override
    public void batchReturnStock(List<ActivityProductSkuReturnStockDTO> returnStockDTOList) {
        activityProductSkuService.batchReturnStock(returnStockDTOList);
    }

    @Override
    public void returnStock(ActivityProductSkuReturnStockDTO returnStockDTO) {
        activityProductSkuService.returnStock(returnStockDTO);
    }
}
