package com.msb.mall.product.service.convert;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.api.model.ProductDO;
import com.msb.mall.product.model.dto.admin.ProductModifyDTO;
import com.msb.mall.product.model.entity.Product;
import com.msb.mall.product.model.vo.admin.ProductModifyVO;
import com.msb.mall.product.model.vo.admin.ProductVO;
import com.msb.mall.product.model.vo.app.ProductDetailVO;
import com.msb.mall.product.model.vo.app.ProductRecommendVO;
import com.msb.mall.product.model.vo.app.ProductSimpleVO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

/**
 * 商品(Product)转换接口
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Mapper(componentModel = "spring")
public interface ProductConvert {

    ProductVO toVo(Product product);

    @Named("toSimpleVo")
    ProductSimpleVO toSimpleVo(Product product);

    @IterableMapping(qualifiedByName = "toSimpleVo")
    List<ProductSimpleVO> toSimpleVo(List<Product> product);

    Page<ProductSimpleVO> toSimpleVo(IPage<Product> product);

    List<ProductVO> toVo(List<Product> product);

    Page<ProductVO> toVo(Page<Product> product);

    Product toEntity(ProductModifyDTO productQueryDTO);

    ProductDetailVO toDetailVo(Product product);

    ProductDO toDo(Product product);

    List<ProductDO> toDo(List<Product> productList);

    ProductRecommendVO toRecommendVo(Product product);

    ProductModifyVO toModifyVO(Product product);
}

