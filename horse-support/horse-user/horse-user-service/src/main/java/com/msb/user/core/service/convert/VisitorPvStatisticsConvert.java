package com.msb.user.core.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.core.model.entity.VisitorPvStatistics;
import com.msb.user.core.model.vo.VisitorPvStatisticsVO;
import com.msb.user.core.model.dto.VisitorPvStatisticsDTO;

import java.util.List;
import org.mapstruct.Mapper;

/**
 * 访客pv统计表(VisitorPvStatistics)表服务接口
 *
 * @author shumengjiao
 * @since 2022-06-07 19:40:45
 */
@Mapper(componentModel = "spring")
public interface VisitorPvStatisticsConvert {

    VisitorPvStatisticsVO toVo(VisitorPvStatistics visitorPvStatistics);

    List<VisitorPvStatisticsVO> toVo(List<VisitorPvStatistics> visitorPvStatistics);

    Page<VisitorPvStatisticsVO> toVo(Page<VisitorPvStatistics> visitorPvStatistics);

    VisitorPvStatistics toDo(VisitorPvStatisticsDTO visitorPvStatisticsDTO);
}

