package com.msb.user.core.job;

import com.msb.user.core.service.VisitorPvStatisticsService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author shumengjiao
 */
@Slf4j
@Component
public class VisitorPvStatisticsJob {
    @Resource
    private VisitorPvStatisticsService visitorPvStatisticsService;
    /**
     * 统计某一天的访客数量保存到访客pv统计表
     */
    @XxlJob("statisticsVisitorPvCount")
    public void statisticsVisitorPvCount() {
        String param = XxlJobHelper.getJobParam();
        log.info("统计访客pv数量，入参：{}", param);
        LocalDate date = LocalDate.now().minusDays(1);
        if (StringUtils.isNotBlank(param)) {
            date = LocalDate.parse(param);
        }
        try {
            visitorPvStatisticsService.statistics(date);
            log.info("统计访客数量成功,date:{}", date);
        } catch (Exception e) {
            log.error("统计访客数量失败,date:{}", date);
        }

    }
}

