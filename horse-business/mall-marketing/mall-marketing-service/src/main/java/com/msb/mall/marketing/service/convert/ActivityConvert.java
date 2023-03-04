package com.msb.mall.marketing.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.marketing.model.dto.SaveActivityDTO;
import com.msb.mall.marketing.model.entity.Activity;
import com.msb.mall.marketing.model.vo.ActivityVO;
import com.msb.mall.marketing.model.dto.PageActivityDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 活动表(Activity)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Mapper(componentModel = "spring")
public interface ActivityConvert {

    ActivityVO toVo(Activity activity);

    List<ActivityVO> toVo(List<Activity> activity);

    Page<ActivityVO> toVo(Page<Activity> activity);

    Activity toEntity(SaveActivityDTO activityDTO);
}

