package com.msb.mall.product.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.product.mapper.ProductDetailMapper;
import com.msb.mall.product.model.entity.ProductDetail;
import org.springframework.stereotype.Service;

/**
 * 商品详情表(ProductDetail)表服务实现类
 *
 * @author luozhan
 * @date 2022-04-11 10:31:50
 */
@Service("productDetailService")
public class ProductDetailService extends ServiceImpl<ProductDetailMapper, ProductDetail> {

    public void update(Long productId, String detail) {
        lambdaUpdate().eq(ProductDetail::getProductId, productId).set(ProductDetail::getDetail, detail).update();
    }
}

