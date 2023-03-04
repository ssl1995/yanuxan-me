package com.msb.mall.trade.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.ListUtil;
import com.msb.mall.trade.api.model.SalesStatisticsByDayDO;
import com.msb.mall.trade.api.model.SalesStatisticsByHourDO;
import com.msb.mall.trade.mapper.SalesStatisticsMapper;
import com.msb.mall.trade.model.entity.SalesStatistics;
import com.msb.mall.trade.service.convert.SalesStatisticsConvert;
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
 * 销售额统计表(SalesStatistics)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-05-30 20:30:19
 */
@Service("salesStatisticsService")
public class SalesStatisticsService extends ServiceImpl<SalesStatisticsMapper, SalesStatistics> {

    @Resource
    private TradeOrderService tradeOrderService;
    @Resource
    private SalesStatisticsMapper salesStatisticsMapper;

    @Resource
    private SalesStatisticsConvert salesStatisticsConvert;

    /**
     * 根据时间区间从订单统计表中获取销售总额
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return 销售总额
     */
    public BigDecimal getSales(LocalDate beginDate, LocalDate endDate) {
        return salesStatisticsMapper.getSales(beginDate, endDate);
    }

    /**
     * 按天统计销售额
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return 销售额
     */
    public List<SalesStatisticsByDayDO> listSalesStatisticsByDay(LocalDate beginDate, LocalDate endDate) {
        // 查询销售额
        List<SalesStatistics> list = salesStatisticsMapper.listSalesStatisticsByDay(beginDate, endDate);
        List<SalesStatisticsByDayDO> queryResultList = salesStatisticsConvert.toDo(list);

        // 获取连续的时间集合
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(beginDate);
        while (beginDate.isBefore(endDate)) {
            beginDate = beginDate.plusDays(1);
            localDateList.add(beginDate);
        }
        // 将时间集合赋值给销售额集合
        List<SalesStatisticsByDayDO> dayList = localDateList.stream().map(item -> {
            SalesStatisticsByDayDO statistics = new SalesStatisticsByDayDO();
            statistics.setRecordDate(item);
            // 今天之后的日期值设置为null
            BigDecimal sales = item.compareTo(LocalDate.now()) > 0 ? null : BigDecimal.ZERO;
            statistics.setSales(sales);
            return statistics;
        }).collect(Collectors.toList());

        ListUtil.match(dayList, queryResultList, SalesStatisticsByDayDO::getRecordDate, (day, queryResult) -> day.setSales(queryResult.getSales()));
        return dayList;
    }

    /**
     * 统计某天的销售额并保存到销售额统计表
     * @param date 日期
     */
    @Transactional(rollbackFor = Exception.class)
    public void statisticsSales(LocalDate date) {
        BigDecimal sales = tradeOrderService.getSales(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
        sales = sales == null ? BigDecimal.ZERO : sales;
        SalesStatistics salesStatistics = new SalesStatistics().setRecordDate(date).setSales(sales);
        SalesStatistics one = this.lambdaQuery().eq(SalesStatistics::getRecordDate, date).one();
        if (one != null) {
            salesStatisticsMapper.deleteById(one.getId());
        }
        save(salesStatistics);
    }
}

