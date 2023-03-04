package com.msb.user.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.framework.common.utils.ListUtil;
import com.msb.framework.redis.RedisClient;
import com.msb.user.api.vo.VisitorStatisticsByDayDO;
import com.msb.user.core.mapper.VisitorUvStatisticsMapper;
import com.msb.user.core.model.constant.RedisKeysConstants;
import com.msb.user.core.model.entity.VisitorUvStatistics;
import com.msb.user.core.service.convert.VisitorUvStatisticsConvert;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 访客统计表(VisitorStatistics)表服务实现类
 *
 * @author shumengjiao
 * @since 2022-06-07 12:55:15
 */
@Service("visitorStatisticsService")
public class VisitorUvStatisticsService extends ServiceImpl<VisitorUvStatisticsMapper, VisitorUvStatistics> {
    @Resource
    private RedisClient redisClient;
    @Resource
    private VisitorUvStatisticsMapper visitorUvStatisticsMapper;
    @Resource
    private VisitorUvStatisticsConvert visitorUvStatisticsConvert;

    /**
     * 统计访客数量 并保存到统计表
     *
     * @param date
     */
    public void statisticsUv(LocalDate date) {
        // 获取该日期在redis中保存的数据值
        Long size = redisClient.pfCount(RedisKeysConstants.VISITOR_UV_KEY + date);
        VisitorUvStatistics visitorUvStatistics = new VisitorUvStatistics().setRecordDate(date).setCount(size.intValue());
        VisitorUvStatistics one = this.lambdaQuery().eq(VisitorUvStatistics::getRecordDate, date).one();
        if (one != null) {
            visitorUvStatisticsMapper.deleteById(one.getId());
        }
        this.save(visitorUvStatistics);
    }

    /**
     * 从访客统计表获取某一段时间的访客数量总和
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return 访客数量总和
     */
    public Integer getVisitorCountFromStatistics(LocalDate beginDate, LocalDate endDate) {
        return visitorUvStatisticsMapper.getVisitorCount(beginDate, endDate);
    }

    /**
     * 按天查询某段时间的访客数量
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 访客数量
     */
    public List<VisitorStatisticsByDayDO> listVisitorStatistics(LocalDate beginDate, LocalDate endDate) {
        // 查询访客数量
        List<VisitorUvStatistics> list = this.lambdaQuery()
                .ge(VisitorUvStatistics::getRecordDate, beginDate)
                .le(VisitorUvStatistics::getRecordDate, endDate)
                .list();
        List<VisitorStatisticsByDayDO> queryResultList = visitorUvStatisticsConvert.toDayDO(list);

        // 获取连续的时间集合
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(beginDate);
        while (beginDate.isBefore(endDate)) {
            beginDate = beginDate.plusDays(1);
            localDateList.add(beginDate);
        }
        // 将时间集合赋值给访客数量集合
        List<VisitorStatisticsByDayDO> dayList = localDateList.stream().map(item -> {
            VisitorStatisticsByDayDO statistics = new VisitorStatisticsByDayDO();
            statistics.setRecordDate(item);
            Integer count = item.compareTo(LocalDate.now()) > 0 ? null : 0;
            statistics.setCount(count);
            return statistics;
        }).collect(Collectors.toList());

        ListUtil.match(dayList, queryResultList, VisitorStatisticsByDayDO::getRecordDate, (day, queryResult) -> day.setCount(queryResult.getCount()));
        return dayList;
    }
}

