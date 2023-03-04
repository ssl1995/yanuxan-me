package com.msb.mall.marketing.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.marketing.model.entity.ActivityTime;
import com.msb.mall.marketing.model.vo.ActivityTimeVO;
import com.msb.mall.marketing.model.dto.ActivityTimeDTO;
import com.msb.mall.marketing.model.vo.app.CurrentActivityTimeVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 活动时段表(ActivityTime)表服务接口
 *
 * @author makejava
 * @date 2022-04-08 14:57:59
 */
@Mapper(componentModel = "spring")
public interface ActivityTimeConvert {

    ActivityTimeVO toVo(ActivityTime activityTime);

    List<ActivityTimeVO> toVo(List<ActivityTime> activityTime);

    List<CurrentActivityTimeVO> toCurrentVo(List<ActivityTime> activityTime);

    Page<ActivityTimeVO> toVo(Page<ActivityTime> activityTime);

    ActivityTime toDo(ActivityTimeDTO activityTimeDTO);
}

