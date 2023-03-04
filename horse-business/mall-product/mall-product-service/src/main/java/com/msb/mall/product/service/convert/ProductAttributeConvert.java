package com.msb.mall.product.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.model.dto.admin.ProductAttributeDTO;
import com.msb.mall.product.model.entity.ProductAttribute;
import com.msb.mall.product.model.vo.admin.ProductAttributeVO;
import com.msb.mall.product.model.vo.app.ProductAttributeSimpleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 商品属性(ProductAttribute)转换接口
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Mapper(componentModel = "spring")
public interface ProductAttributeConvert {

    ProductAttributeVO toVo(ProductAttribute productAttribute);

    List<ProductAttributeVO> toVo(List<ProductAttribute> productAttribute);

    Page<ProductAttributeVO> toVo(Page<ProductAttribute> productAttribute);

    ProductAttribute toEntity(ProductAttributeDTO productAttributeDTO);

    List<ProductAttribute> toEntity(List<ProductAttributeDTO> productAttributeDTO);

    List<ProductAttributeSimpleVO> toSimpleVo(List<ProductAttribute> result);
}

