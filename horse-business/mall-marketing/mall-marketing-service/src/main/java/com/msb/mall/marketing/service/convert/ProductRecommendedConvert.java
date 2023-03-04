package com.msb.mall.marketing.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.marketing.model.entity.ProductRecommended;
import com.msb.mall.marketing.model.vo.ProductRecommendedVO;
import com.msb.mall.marketing.model.dto.ProductRecommendedDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 商品推荐表(ProductRecommended)表服务接口
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Mapper(componentModel = "spring")
public interface ProductRecommendedConvert {

    ProductRecommendedVO toVo(ProductRecommended productRecommended);

    List<ProductRecommendedVO> toVo(List<ProductRecommended> productRecommended);

    Page<ProductRecommendedVO> toVo(Page<ProductRecommended> productRecommended);

    ProductRecommended toDo(ProductRecommendedDTO productRecommendedDTO);
}

