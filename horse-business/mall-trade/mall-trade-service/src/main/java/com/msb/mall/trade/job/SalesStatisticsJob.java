package com.msb.mall.trade.job;

import com.msb.framework.common.utils.DateUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.trade.service.SalesStatisticsService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 销售额统计定时任务
 * @author shumengjiao
 */
@Slf4j
@Component
public class SalesStatisticsJob {

    @Resource
    private SalesStatisticsService salesStatisticsService;

    /**
     * 统计某一天的销售额保存到销售额统计表
     */
    @XxlJob("statisticsSales")
    public void statisticsSales() {
        String param = XxlJobHelper.getJobParam();
        log.info("统计销售额，入参：{}", param);
        LocalDate date = LocalDate.now().minusDays(1);
        if (StringUtils.isNotBlank(param)) {
            date = LocalDate.parse(param);
        }
        try {
            salesStatisticsService.statisticsSales(date);
            log.info("统计销售额成功,date:{}", date);
        } catch (Exception e) {
            log.error("统计销售额失败,date:{}", date);
        }

    }
}
