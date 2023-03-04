package com.msb.mall.product.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.model.dto.admin.ProductAttributeGroupDTO;
import com.msb.mall.product.model.entity.ProductAttributeGroup;
import com.msb.mall.product.model.vo.admin.ProductAttributeGroupVO;
import com.msb.mall.product.model.vo.app.ProductAttributeGroupSimpleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 商品属性组(ProductAttributeGroup)转换接口
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Mapper(componentModel = "spring")
public interface ProductAttributeGroupConvert {

    ProductAttributeGroupVO toVo(ProductAttributeGroup productAttributeGroup);

    List<ProductAttributeGroupVO> toVo(List<ProductAttributeGroup> productAttributeGroup);

    Page<ProductAttributeGroupVO> toVo(Page<ProductAttributeGroup> productAttributeGroup);

    ProductAttributeGroup toEntity(ProductAttributeGroupDTO productAttributeGroupDTO);

    List<ProductAttributeGroupSimpleVO> toSimpleVo(List<ProductAttributeGroup> productAttributeGroup);
}

