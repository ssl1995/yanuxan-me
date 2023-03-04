package com.msb.mall.trade.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.trade.api.model.SalesStatisticsByDayDO;
import com.msb.mall.trade.model.entity.SalesStatistics;
import com.msb.mall.trade.model.vo.admin.SalesStatisticsVO;
import com.msb.mall.trade.model.dto.admin.SalesStatisticsDTO;

import java.util.List;
import org.mapstruct.Mapper;

/**
 * 销售额统计表(SalesStatistics)表服务接口
 *
 * @author shumengjiao
 * @since 2022-05-30 20:30:19
 */
@Mapper(componentModel = "spring")
public interface SalesStatisticsConvert {

    SalesStatisticsVO toVo(SalesStatistics salesStatistics);

    List<SalesStatisticsVO> toVo(List<SalesStatistics> salesStatistics);

    Page<SalesStatisticsVO> toVo(Page<SalesStatistics> salesStatistics);

    SalesStatistics toDo(SalesStatisticsDTO salesStatisticsDTO);

    List<SalesStatisticsByDayDO> toDo(List<SalesStatistics> list);
}

