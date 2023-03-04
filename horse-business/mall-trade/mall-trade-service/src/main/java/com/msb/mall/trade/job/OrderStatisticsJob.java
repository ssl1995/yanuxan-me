package com.msb.mall.trade.job;

import com.msb.framework.common.utils.DateUtil;
import com.msb.framework.web.result.BizAssert;
import com.msb.mall.trade.service.OrderStatisticsService;
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
 * 订单统计定时任务
 */
@Slf4j
@Component
public class OrderStatisticsJob {
    @Resource
    private OrderStatisticsService orderStatisticsService;

    /**
     * 统计某一天的订单数量保存到订单统计表
     */
    @XxlJob("statisticsOrderCount")
    public void statisticsOrderCount() {
        String param = XxlJobHelper.getJobParam();
        log.info("统计订单数量，入参：{}", param);
        LocalDate date = LocalDate.now().minusDays(1);
        if (StringUtils.isNotBlank(param)) {
            date = LocalDate.parse(param);
        }
        try {
            orderStatisticsService.statisticsOrder(date);
            log.info("统计订单数量成功,date:{}", date);
        } catch (Exception e) {
            log.error("统计订单数量失败,date:{}", date);
        }

    }
}
