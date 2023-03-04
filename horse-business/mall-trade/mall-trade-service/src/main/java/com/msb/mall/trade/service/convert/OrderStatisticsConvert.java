package com.msb.mall.trade.service.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.trade.api.model.OrderStatisticsByDayDO;
import com.msb.mall.trade.model.entity.OrderStatistics;
import com.msb.mall.trade.model.vo.admin.OrderStatisticsVO;
import com.msb.mall.trade.model.dto.admin.OrderStatisticsDTO;

import java.util.List;
import org.mapstruct.Mapper;

/**
 * 订单统计表(OrderStatistics)表服务接口
 *
 * @author shumengjiao
 * @since 2022-05-30 20:17:31
 */
@Mapper(componentModel = "spring")
public interface OrderStatisticsConvert {

    OrderStatisticsVO toVo(OrderStatistics orderStatistics);

    List<OrderStatisticsVO> toVo(List<OrderStatistics> orderStatistics);

    Page<OrderStatisticsVO> toVo(Page<OrderStatistics> orderStatistics);

    OrderStatistics toDo(OrderStatisticsDTO orderStatisticsDTO);

    List<OrderStatisticsByDayDO> toDayDO(List<OrderStatistics> list);
}

