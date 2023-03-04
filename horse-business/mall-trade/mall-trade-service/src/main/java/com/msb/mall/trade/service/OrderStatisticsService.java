package com.msb.mall.trade.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.ListUtil;
import com.msb.mall.trade.api.model.OrderStatisticsByDayDO;
import com.msb.mall.trade.api.model.SalesStatisticsByDayDO;
import com.msb.mall.trade.mapper.OrderStatisticsMapper;
import com.msb.mall.trade.model.entity.OrderStatistics;
import com.msb.mall.trade.model.vo.admin.OrderStatisticsVO;
import com.msb.mall.trade.model.dto.admin.OrderStatisticsDTO;
import com.msb.mall.trade.service.convert.OrderStatisticsConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单统计表(OrderStatistics)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:17:31
 */
@Service("orderStatisticsService")
public class OrderStatisticsService extends ServiceImpl<OrderStatisticsMapper, OrderStatistics> {

    @Resource
    private OrderStatisticsMapper orderStatisticsMapper;
    @Resource
    private OrderStatisticsConvert orderStatisticsConvert;
    @Resource
    private TradeOrderService tradeOrderService;

    public List<OrderStatisticsVO> list(OrderStatisticsDTO orderStatisticsDTO) {
        List<OrderStatistics> list = this.lambdaQuery()
                .ge(OrderStatistics::getRecordDate, orderStatisticsDTO.getRecordDateBegin())
                .le(OrderStatistics::getRecordDate, orderStatisticsDTO.getRecordDateEnd()).list();
        return orderStatisticsConvert.toVo(list);
    }

    public Integer getOrderCount(LocalDate beginDate, LocalDate endDate) {
        return orderStatisticsMapper.getOrderCount(beginDate, endDate);
    }

    /**
     * 按天获取订单数量
     *
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 订单数量
     */
    public List<OrderStatisticsByDayDO> listOrderStatisticsByDay(LocalDate beginDate, LocalDate endDate) {
        // 查询订单数量
        List<OrderStatistics> list = orderStatisticsMapper.listOrderStatisticsByDay(beginDate, endDate);
        List<OrderStatisticsByDayDO> queryResultList = orderStatisticsConvert.toDayDO(list);

        // 获取连续的时间集合
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(beginDate);
        while (beginDate.isBefore(endDate)) {
            beginDate = beginDate.plusDays(1);
            localDateList.add(beginDate);
        }
        // 将时间集合赋值给订单额集合
        List<OrderStatisticsByDayDO> dayList = localDateList.stream().map(item -> {
            OrderStatisticsByDayDO statistics = new OrderStatisticsByDayDO();
            statistics.setRecordDate(item);
            Integer count = item.compareTo(LocalDate.now()) > 0 ? null : 0;
            statistics.setCount(count);
            return statistics;
        }).collect(Collectors.toList());

        ListUtil.match(dayList, queryResultList, OrderStatisticsByDayDO::getRecordDate, (day, queryResult) -> day.setCount(queryResult.getCount()));
        return dayList;
    }


    /**
     * 统计订单数量并保存到订单统计表
     * @param date 统计日期
     */
    @Transactional(rollbackFor = Exception.class)
    public void statisticsOrder(LocalDate date) {
        Integer orderCount = tradeOrderService.getOrderCount(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
        OrderStatistics orderStatistics = new OrderStatistics().setRecordDate(date).setCount(orderCount);
        OrderStatistics one = this.lambdaQuery().eq(OrderStatistics::getRecordDate, date).one();
        if (one != null) {
            orderStatisticsMapper.deleteById(one.getId());
        }
        save(orderStatistics);
    }
}

