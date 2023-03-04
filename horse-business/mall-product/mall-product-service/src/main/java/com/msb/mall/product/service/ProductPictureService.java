package com.msb.mall.product.service;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.mall.product.mapper.ProductPictureMapper;
import com.msb.mall.product.model.entity.ProductPicture;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 商品图片(ProductPicture)表服务实现类
 *
 * @author luozhan
 * @date 2022-04-02 20:43:31
 */
@Service
public class ProductPictureService extends ServiceImpl<ProductPictureMapper, ProductPicture> {


    public List<String> listProductPictureByProductId(Long productId) {
        List<ProductPicture> productPictureList = lambdaQuery().select(ProductPicture::getPicture)
                .eq(ProductPicture::getProductId, productId)
                .orderByAsc(ProductPicture::getSort)
                .list();
        return productPictureList.stream().map(ProductPicture::getPicture).collect(Collectors.toList());
    }

    /**
     * 保存商品图片（每次保存删除原有的）
     *
     * @param productId   商品id
     * @param pictureList 图片地址列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void savePictureList(Long productId, List<String> pictureList) {
        remove(Wrappers.<ProductPicture>lambdaQuery().eq(ProductPicture::getProductId, productId));
        List<ProductPicture> productPictureList = IntStream.range(0, pictureList.size())
                .mapToObj(i -> new ProductPicture().setProductId(productId).setPicture(pictureList.get(i)).setSort(i))
                .collect(Collectors.toList());
        saveBatch(productPictureList);
    }
}

