package com.msb.mall.marketing.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.marketing.model.entity.ActivityProduct;
import com.msb.mall.marketing.model.vo.ActivityProductVO;
import com.msb.mall.marketing.model.dto.ActivityProductDTO;
import com.msb.mall.marketing.model.vo.app.AppActivityProductVO;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * 活动商品表(ActivityProduct)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Mapper(componentModel = "spring")
public interface ActivityProductConvert {

    ActivityProductVO toVo(ActivityProduct activityProduct);

    List<ActivityProductVO> toVo(List<ActivityProduct> activityProduct);

    Page<ActivityProductVO> toVo(Page<ActivityProduct> activityProduct);

}
