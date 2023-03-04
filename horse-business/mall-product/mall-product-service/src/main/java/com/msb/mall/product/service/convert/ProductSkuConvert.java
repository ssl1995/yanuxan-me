package com.msb.mall.product.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.api.model.ProductSkuDO;
import com.msb.mall.product.model.dto.admin.ProductSkuDTO;
import com.msb.mall.product.model.entity.ProductSku;
import com.msb.mall.product.model.vo.admin.ProductSkuVO;
import com.msb.mall.product.model.vo.app.ProductSkuSimpleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 商品sku(ProductSku)转换接口
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Mapper(componentModel = "spring")
public interface ProductSkuConvert {

    ProductSkuVO toVo(ProductSku productSku);

    @Mapping(target = "skuId", source = "id")
    ProductSkuSimpleVO toSimpleVo(ProductSku productSku);

    List<ProductSkuSimpleVO> toSimpleVo(List<ProductSku> productSku);

    List<ProductSkuVO> toVo(List<ProductSku> productSku);

    Page<ProductSkuVO> toVo(Page<ProductSku> productSku);

    ProductSku toEntity(ProductSkuDTO productSkuDTO);

    List<ProductSkuDO> toDo(List<ProductSku> productSkuList);

    @Mapping(target = "skuId", source = "id")
    @Mapping(target = "skuName", source = "name")
    ProductSkuDO toDo(ProductSku productSku);
}

