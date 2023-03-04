package com.msb.mall.product.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.model.dto.admin.ProductCategoryModifyDTO;
import com.msb.mall.product.model.entity.ProductCategory;
import com.msb.mall.product.model.vo.admin.ProductCategoryVO;
import com.msb.mall.product.model.vo.app.ProductCategoryAppVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 商品分类(ProductCategory)转换接口
 *
 * @author luozhan
 * @date 2022-03-29 14:37:40
 */
@Mapper(componentModel = "spring")
public interface ProductCategoryConvert {

    ProductCategoryVO toVo(ProductCategory productCategory);

    List<ProductCategoryVO> toVo(List<ProductCategory> productCategory);

    Page<ProductCategoryVO> toVo(Page<ProductCategory> productCategory);

    List<ProductCategoryAppVO> toAppVo(List<ProductCategory> productCategory);

    ProductCategory toEntity(ProductCategoryModifyDTO productCategoryModifyDTO);
}

