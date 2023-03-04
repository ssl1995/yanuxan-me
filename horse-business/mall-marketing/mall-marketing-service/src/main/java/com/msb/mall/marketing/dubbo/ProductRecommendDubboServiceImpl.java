package com.msb.mall.marketing.dubbo;

import com.msb.mall.marketing.api.ProductRecommendDubboService;
import com.msb.mall.marketing.model.dto.ProductRecommendedDTO;
import com.msb.mall.marketing.service.ProductRecommendedService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Collections;

@DubboService
public class ProductRecommendDubboServiceImpl implements ProductRecommendDubboService {

    @Resource
    private ProductRecommendedService productRecommendedService;

    @Override
    public Boolean setProductRecommend(Long productId) {
        ProductRecommendedDTO productRecommendedDTO = new ProductRecommendedDTO();
        productRecommendedDTO.setProductIdList(Collections.singletonList(productId));
        return productRecommendedService.save(productRecommendedDTO);
    }

    @Override
    public Boolean delProductRecommend(Long productId) {
        return productRecommendedService.delete(productId);
    }


    @Override
    public Boolean getProductRecommend(Long productId) {
        return productRecommendedService.getProductIsRecommended(productId);
    }
}
