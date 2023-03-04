package com.msb.user.core.service.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.user.api.vo.VisitorStatisticsByDayDO;
import com.msb.user.core.model.dto.VisitorUvStatisticsDTO;
import com.msb.user.core.model.entity.VisitorUvStatistics;
import com.msb.user.core.model.vo.VisitorUvStatisticsVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 访客统计表(VisitorStatistics)表服务接口
 *
 * @author shumengjiao
 * @since 2022-06-07 12:55:15
 */
@Mapper(componentModel = "spring")
public interface VisitorUvStatisticsConvert {

    VisitorUvStatisticsVO toVo(VisitorUvStatistics visitorUvStatistics);

    List<VisitorUvStatisticsVO> toVo(List<VisitorUvStatistics> visitorUvStatistics);

    Page<VisitorUvStatisticsVO> toVo(Page<VisitorUvStatistics> visitorStatistics);

    VisitorUvStatistics toDo(VisitorUvStatisticsDTO visitorUvStatisticsDTO);

    List<VisitorStatisticsByDayDO> toDayDO(List<VisitorUvStatistics> list);
}

