package com.msb.mall.marketing.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.marketing.model.dto.AdvertisementModifyDTO;
import com.msb.mall.marketing.model.entity.Advertisement;
import com.msb.mall.marketing.model.vo.AdvertisementVO;
import com.msb.mall.marketing.model.vo.app.AdvertisementSimpleVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 广告(Advertisement)表服务接口
 *
 * @author shumengjiao
 * @since 2022-05-21 18:08:46
 */
@Mapper(componentModel = "spring")
public interface AdvertisementConvert {

    AdvertisementVO toVo(Advertisement advertisement);

    List<AdvertisementVO> toVo(List<Advertisement> advertisement);

    Page<AdvertisementVO> toVo(Page<Advertisement> advertisement);

    Advertisement toEntity(AdvertisementModifyDTO advertisementDTO);

    List<AdvertisementSimpleVO> toSimpleVo(List<Advertisement> list);
}

