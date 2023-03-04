package com.msb.user.core.dubbo;

import com.msb.framework.redis.RedisClient;
import com.msb.user.api.VisitorDubboService;
import com.msb.user.api.vo.VisitorStatisticsByDayDO;
import com.msb.user.core.model.constant.RedisKeysConstants;
import com.msb.user.core.service.VisitorUvStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author shumengjiao
 */
@Slf4j
@DubboService(timeout = 5000)
public class VisitorDubboServiceImpl implements VisitorDubboService {
    @Resource
    private RedisClient redisClient;
    @Resource
    private VisitorUvStatisticsService visitorUvStatisticsService;

    /**
     * 从Redis中获取某一天的uv访客数量
     * @param date 日期
     * @return 访客数量
     */
    @Override
    public Integer getVisitorCount(LocalDate date) {
        // 从redis中获取
        String key = RedisKeysConstants.VISITOR_UV_KEY;
        Long size = redisClient.pfCount(key + LocalDate.now());
        return size.intValue();
    }

    /**
     *  从访客统计表获取某一段时间的访客数量总和
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 访客数量总和
     */
    @Override
    public Integer getVisitorCountByStatistics(LocalDate beginDate, LocalDate endDate) {
        return visitorUvStatisticsService.getVisitorCountFromStatistics(beginDate, endDate);
    }

    /**
     * 按天查询某段时间的访客数量
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 访客数量
     */
    @Override
    public List<VisitorStatisticsByDayDO> listVisitorStatisticsByDay(LocalDate beginDate, LocalDate endDate) {
        return visitorUvStatisticsService.listVisitorStatistics(beginDate, endDate);
    }

}
