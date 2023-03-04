package com.msb.mall.marketing.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.marketing.api.model.ProductSkuDO;
import com.msb.mall.marketing.model.entity.ActivityProductSku;
import com.msb.mall.marketing.model.vo.ActivityProductSkuVO;
import com.msb.mall.marketing.model.dto.ActivityProductSkuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 活动商品表(ActivityProductSku)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Mapper(componentModel = "spring")
public interface ActivityProductSkuConvert {

    ActivityProductSkuVO toVo(ActivityProductSku activityProductSku);

    List<ActivityProductSkuVO> toVo(List<ActivityProductSku> activityProductSku);

    Page<ActivityProductSkuVO> toVo(Page<ActivityProductSku> activityProductSku);

    ActivityProductSku toEntity(ActivityProductSkuDTO activityProductSkuDTO);

    ProductSkuDO toDO(ActivityProductSku activityProductSku);

    List<ProductSkuDO> toDO(List<ActivityProductSku> activityProductSku);
}

