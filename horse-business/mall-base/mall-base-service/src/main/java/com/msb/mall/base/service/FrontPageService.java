package com.msb.mall.base.service;

import com.msb.framework.common.utils.DateUtil;
import com.msb.mall.base.model.dto.FrontPageOrderQueryDTO;
import com.msb.mall.base.model.dto.FrontPageSalesQueryDTO;
import com.msb.mall.base.model.dto.FrontPageVisitorQueryDTO;
import com.msb.mall.base.model.vo.*;
import com.msb.mall.base.service.convert.FrontPageConvert;
import com.msb.mall.product.api.dubbo.ProductDubboService;
import com.msb.mall.trade.api.dubbo.TradeOrderDubboService;
import com.msb.mall.trade.api.model.OrderStatisticsByDayDO;
import com.msb.mall.trade.api.model.SalesStatisticsByDayDO;
import com.msb.user.api.UserDubboService;
import com.msb.user.api.VisitorDubboService;
import com.msb.user.api.vo.VisitorStatisticsByDayDO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;

/**
 * @author shumengjiao
 */
@Service("frontPageService")
public class FrontPageService {
    @Resource
    private FrontPageConvert frontPageConvert;

    @DubboReference
    private ProductDubboService productDubboService;
    @DubboReference
    private TradeOrderDubboService tradeOrderDubboService;
    @DubboReference
    private UserDubboService userDubboService;
    @DubboReference
    private VisitorDubboService visitorDubboService;

    /*LocalDateTime todayStart = DateUtil.todayStart();*/
    /*LocalDateTime todayEnd = DateUtil.todayEnd();*/
    /*LocalDate lastWeekFirstDay = DateUtil.getLastWeekMonday();*/
    /*LocalDate lastWeekLastDay = DateUtil.getLastWeekSunday();*/
    /*LocalDate lastMonthFirstDay = DateUtil.getLastMonthFirstDay();*/
    /*LocalDate lastMonthLastDay = DateUtil.getLastMonthLastDay();*/
    /*LocalDate thisWeekFirstDay = DateUtil.getWeekOfMonday();*/
    /*LocalDate thisMonthFirstDay = DateUtil.getMonthFirstDay();*/
    /*LocalDate today = LocalDate.now();*/
    /*LocalDate yesterday = DateUtil.getLastDay();*/

    /**
     * 获取头部数据 今日访客 今日订单 今日销售额 近七天销售额
     *
     * @return FrontPageHeaderDataVO
     */
    public FrontPageHeaderDataVO getHeaderData() {
        LocalDateTime todayStart = DateUtil.todayStart();
        LocalDateTime todayEnd = DateUtil.todayEnd();
        LocalDate today = LocalDate.now();
        // 今日访客
        Integer todayVisitor = visitorDubboService.getVisitorCount(today);
        // 今日订单总数
        Integer orderCount = tradeOrderDubboService.getOrderCount(todayStart, todayEnd);
        // 今日销售总额
        BigDecimal todaySales = tradeOrderDubboService.getSalesByTradeOrder(todayStart, todayEnd);
        todaySales = todaySales == null ? BigDecimal.ZERO : todaySales;
        // 近七天销售额
        BigDecimal recentSales = tradeOrderDubboService.getSalesByOrderStatistics(today.minusDays(6), today.minusDays(1));
        recentSales = recentSales == null ? BigDecimal.ZERO : recentSales;
        recentSales = recentSales.add(todaySales);

        return new FrontPageHeaderDataVO()
                .setTodayVisitorCount(todayVisitor)
                .setTodayOrderCount(orderCount)
                .setTodaySales(todaySales)
                .setLastSevenDaysSales(recentSales);
    }

    /**
     * 获取待处理事务数据
     *
     * @return FrontPageWaitHandleVO
     */
    public FrontPageWaitHandleVO getWaitHandleAffairs() {
        return frontPageConvert.toVo(tradeOrderDubboService.getWaitHandleOrderCount());
    }

    /**
     * 获取商品总览数据
     *
     * @return FrontPageProductVO
     */
    public FrontPageProductVO getProductOverview() {
        return frontPageConvert.toVo(productDubboService.getProductOverview());
    }

    /**
     * 获取用户总览数据
     *
     * @return FrontPageUserVO
     */
    public FrontPageUserVO getUserOverviewData() {
        return frontPageConvert.toVo(userDubboService.getUserOverview());
    }

    /**
     * 获取销售额同比增长数据
     *
     * @return FrontPageSalesTotalVO
     */
    public FrontPageSalesTotalVO getSalesTotal() {
        LocalDateTime todayStart = DateUtil.todayStart();
        LocalDateTime todayEnd = DateUtil.todayEnd();
        LocalDate lastWeekFirstDay = DateUtil.getLastWeekMonday();
        LocalDate lastWeekLastDay = DateUtil.getLastWeekSunday();
        LocalDate lastMonthFirstDay = DateUtil.getLastMonthFirstDay();
        LocalDate lastMonthLastDay = DateUtil.getLastMonthLastDay();
        LocalDate thisWeekFirstDay = DateUtil.getWeekOfMonday();
        LocalDate thisMonthFirstDay = DateUtil.getMonthFirstDay();
        // 1、获取今日的销售额
        BigDecimal todaySales = tradeOrderDubboService.getSalesByTradeOrder(todayStart, todayEnd);
        todaySales = todaySales == null ? BigDecimal.ZERO : todaySales;
        // 2、获取本周销售额
        BigDecimal weekSales = getSalesToToday(todaySales, thisWeekFirstDay);

        // 3、获取上周销售总额
        BigDecimal lastWeekSales = tradeOrderDubboService.getSalesByOrderStatistics(lastWeekFirstDay, lastWeekLastDay);
        lastWeekSales = lastWeekSales == null ? BigDecimal.ZERO : lastWeekSales;

        // 4、获取本月销售总额 今日销售额+本月一号到昨天的销售额
        BigDecimal monthSales = getSalesToToday(todaySales, thisMonthFirstDay);

        // 5、获取上月销售总额
        BigDecimal lastMonthSales = tradeOrderDubboService.getSalesByOrderStatistics(lastMonthFirstDay, lastMonthLastDay);
        lastMonthSales = lastMonthSales == null ? BigDecimal.ZERO : lastMonthSales;

        // 6、本周销售额同比上周涨跌 （本周-上周）÷上周
        BigDecimal weekGain = BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(lastWeekSales) != 0) {
            weekGain = (weekSales.subtract(lastWeekSales)).divide(lastWeekSales, 4, BigDecimal.ROUND_HALF_UP);
        }

        // 7、本月销售额同比上月涨跌
        BigDecimal monthGain = BigDecimal.ZERO;
        if (BigDecimal.ZERO.compareTo(lastMonthSales) != 0) {
            monthGain = (monthSales.subtract(lastMonthSales)).divide(lastMonthSales, 4, BigDecimal.ROUND_HALF_UP);
        }

        return new FrontPageSalesTotalVO()
                .setWeekSales(weekSales)
                .setWeekUpsOrDownsScale(weekGain)
                .setMonthSales(monthSales)
                .setMonthUpsOrDownsScale(monthGain);
    }

    /**
     * 获取从某一天到今天的销售总额
     * @param todaySales 今日销售额
     * @return 销售总额
     */
    public BigDecimal getSalesToToday(BigDecimal todaySales, LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = DateUtil.getLastDay();
        BigDecimal salesExceptToday = BigDecimal.ZERO;
        // 当今天不是第一天时查询统计表数据，salesExceptToday=0
        if (date.compareTo(today) != 0) {
            // 开始日期到昨日的销售额从销售额统计表获取
            salesExceptToday = tradeOrderDubboService.getSalesByOrderStatistics(date, yesterday);
            salesExceptToday = salesExceptToday == null ? BigDecimal.ZERO : salesExceptToday;
        }
        return salesExceptToday.add(todaySales);
    }

    /**
     * 获取今日销售额统计数据
     *
     * @return list
     */
    public List<FrontPageSalesStatisticsByHourVO> listTodaySalesStatistics() {
        LocalDate today = LocalDate.now();
        return frontPageConvert.toSalesHourVo(tradeOrderDubboService.listSalesStatisticsByHour(today));
    }

    /**
     * 获取销售额统计数据
     *
     * @param frontPageSalesQueryDTO 查询dto
     * @return list 销售额统计数据
     */
    public List<FrontPageSalesStatisticsByDayVO> listSalesStatistics(FrontPageSalesQueryDTO frontPageSalesQueryDTO) {
        LocalDateTime todayStart = DateUtil.todayStart();
        LocalDateTime todayEnd = DateUtil.todayEnd();
        LocalDate beginDate = frontPageSalesQueryDTO.getBeginDate();
        LocalDate endDate = frontPageSalesQueryDTO.getEndDate();
        LocalDate today = LocalDate.now();

        List<SalesStatisticsByDayDO> list = tradeOrderDubboService.listSalesStatisticsByDay(beginDate, endDate);
        // 如果结束的日期为今天或者今天之后，需要查询今天的销售额
        if (today.compareTo(endDate) <= 0) {
            BigDecimal todaySales = tradeOrderDubboService.getSalesByTradeOrder(todayStart, todayEnd);
            todaySales = todaySales == null ? BigDecimal.ZERO : todaySales;
            for (SalesStatisticsByDayDO statistics : list) {
                if (today.compareTo(statistics.getRecordDate()) == 0) {
                    statistics.setSales(todaySales);
                }
            }
        }
        return frontPageConvert.toSalesDayVo(list);
    }

    /**
     * 获取订单数量同比增长数据
     *
     * @return FrontPageOrderTotalVO
     */
    public FrontPageOrderTotalVO getOrderTotal() {
        LocalDateTime todayStart = DateUtil.todayStart();
        LocalDateTime todayEnd = DateUtil.todayEnd();
        LocalDate lastWeekFirstDay = DateUtil.getLastWeekMonday();
        LocalDate lastWeekLastDay = DateUtil.getLastWeekSunday();
        LocalDate lastMonthFirstDay = DateUtil.getLastMonthFirstDay();
        LocalDate lastMonthLastDay = DateUtil.getLastMonthLastDay();
        LocalDate thisWeekFirstDay = DateUtil.getWeekOfMonday();
        LocalDate thisMonthFirstDay = DateUtil.getMonthFirstDay();
        // 1、获取今日订单数量
        Integer todayOrderCount = tradeOrderDubboService.getOrderCount(todayStart, todayEnd);
        todayOrderCount = todayOrderCount == null ? 0 : todayOrderCount;

        // 2、获取本周订单数量
        Integer thisWeekOrderCount = getOrderCountToToday(todayOrderCount, thisWeekFirstDay);

        // 3、获取上周订单数量
        Integer lastWeekOrderCount = tradeOrderDubboService.getOrderCountByStatistics(lastWeekFirstDay, lastWeekLastDay);
        lastWeekOrderCount = lastWeekOrderCount == null ? 0 : lastWeekOrderCount;

        // 4、获取本月订单数量
        Integer thisMonthOrderCount = getOrderCountToToday(todayOrderCount, thisMonthFirstDay);

        // 5、获取上月订单数量
        Integer lastMonthOrderCount = tradeOrderDubboService.getOrderCountByStatistics(lastMonthFirstDay, lastMonthLastDay);
        lastMonthOrderCount = lastMonthOrderCount == null ? 0 : lastMonthOrderCount;

        // 6、计算本周同比上周涨幅
        BigDecimal weekGain = BigDecimal.ZERO;
        if (lastWeekOrderCount != 0) {
            weekGain = BigDecimal.valueOf((thisWeekOrderCount - lastWeekOrderCount)).divide(BigDecimal.valueOf(lastWeekOrderCount), 4, BigDecimal.ROUND_HALF_UP);
        }

        // 7、计算本月同比上月涨幅
        BigDecimal monthGain = BigDecimal.ZERO;
        if (lastMonthOrderCount != 0) {
            monthGain = BigDecimal.valueOf((thisMonthOrderCount - lastMonthOrderCount)).divide(BigDecimal.valueOf(lastMonthOrderCount), 4, BigDecimal.ROUND_HALF_UP);
        }

        return new FrontPageOrderTotalVO()
                .setWeekOrder(thisWeekOrderCount)
                .setWeekUpsOrDownsScale(weekGain)
                .setMonthOrder(thisMonthOrderCount)
                .setMonthUpsOrDownsScale(monthGain);
    }

    /**
     * 获取某个日期到今天的订单总数量
     * @param todayOrderCount 今日订单数量
     * @param date 开始日期
     * @return 订单总数量
     */
    public Integer getOrderCountToToday(Integer todayOrderCount, LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = DateUtil.getLastDay();
        Integer orderCountExceptToday = 0;
        // 当今天不是第一天时查询统计表数据，orderCountExceptToday=0
        if (date.compareTo(today) != 0) {
            // 开始日期到昨日的订单数量从订单统计表获取
            orderCountExceptToday = tradeOrderDubboService.getOrderCountByStatistics(date, yesterday);
            orderCountExceptToday = orderCountExceptToday == null ? 0 : orderCountExceptToday;
        }
        return orderCountExceptToday + todayOrderCount;
    }

    /**
     * 获取今日订单统计数据
     *
     * @return list
     */
    public List<FrontPageOrderStatisticsByHourVO> listTodayOrderStatistics() {
        LocalDate today = LocalDate.now();
        return frontPageConvert.toOrderHourVo(tradeOrderDubboService.listOrderStatisticsByHour(today));

    }

    /**
     * 获取订单统计数据
     *
     * @param frontPageOrderQueryDTO 查询DTO
     * @return list
     */
    public List<FrontPageOrderStatisticsByDayVO> listOrderStatistics(FrontPageOrderQueryDTO frontPageOrderQueryDTO) {
        LocalDateTime todayStart = DateUtil.todayStart();
        LocalDateTime todayEnd = DateUtil.todayEnd();
        LocalDate today = LocalDate.now();
        LocalDate beginDate = frontPageOrderQueryDTO.getBeginDate();
        LocalDate endDate = frontPageOrderQueryDTO.getEndDate();
        List<OrderStatisticsByDayDO> list = tradeOrderDubboService.listOrderStatisticsByDay(beginDate, endDate);
        // 如果结束时间为今天或者今天之后，需要查询今日订单数据
        if (today.compareTo(endDate) <= 0) {
            Integer todayOrderCount = tradeOrderDubboService.getOrderCount(todayStart, todayEnd);
            for (OrderStatisticsByDayDO statistics : list) {
                if (today.compareTo(statistics.getRecordDate()) == 0) {
                    statistics.setCount(todayOrderCount);
                }
            }
        }
        return frontPageConvert.toOrderDayVO(list);
    }

    /**
     * 获取访客数量同比增长数据
     *
     * @return FrontPageVisitorTotalVO
     */
    public FrontPageVisitorTotalVO getVisitorTotal() {
        LocalDate lastWeekFirstDay = DateUtil.getLastWeekMonday();
        LocalDate lastWeekLastDay = DateUtil.getLastWeekSunday();
        LocalDate lastMonthFirstDay = DateUtil.getLastMonthFirstDay();
        LocalDate lastMonthLastDay = DateUtil.getLastMonthLastDay();
        LocalDate thisWeekFirstDay = DateUtil.getWeekOfMonday();
        LocalDate thisMonthFirstDay = DateUtil.getMonthFirstDay();
        LocalDate today = LocalDate.now();
        // 1、获取今日访客数量
        Integer todayVisitorCount = visitorDubboService.getVisitorCount(today);

        // 2、获取本周访客数量
        Integer thisWeekVisitorCount = getVisitorCountToToday(todayVisitorCount, thisWeekFirstDay);

        // 3、获取上周访客数量
        Integer lastWeekVisitorCount = visitorDubboService.getVisitorCountByStatistics(lastWeekFirstDay, lastWeekLastDay);
        lastWeekVisitorCount = lastWeekVisitorCount == null ? 0 : lastWeekVisitorCount;

        // 4、获取本月订单数量
        Integer thisMonthVisitorCount = getVisitorCountToToday(todayVisitorCount, thisMonthFirstDay);

        // 5、获取上月订单数量
        Integer lastMonthVisitorCount = visitorDubboService.getVisitorCountByStatistics(lastMonthFirstDay, lastMonthLastDay);
        lastMonthVisitorCount = lastMonthVisitorCount == null ? 0 : lastMonthVisitorCount;
        
        // 6、计算本周同比上周涨幅
        BigDecimal weekVisitorGain = BigDecimal.ZERO;
        if (lastWeekVisitorCount != 0) {
            weekVisitorGain = BigDecimal.valueOf((thisWeekVisitorCount - lastWeekVisitorCount)).divide(BigDecimal.valueOf(lastWeekVisitorCount), 4, BigDecimal.ROUND_HALF_UP);
        }

        // 7、计算本月同比上月涨幅
        BigDecimal monthVisitorGain = BigDecimal.ZERO;
        if (lastMonthVisitorCount != 0) {
            monthVisitorGain = BigDecimal.valueOf((thisMonthVisitorCount - lastMonthVisitorCount)).divide(BigDecimal.valueOf(lastMonthVisitorCount), 4, BigDecimal.ROUND_HALF_UP);
        }

        return new FrontPageVisitorTotalVO()
                .setWeekVisitor(thisWeekVisitorCount)
                .setWeekUpsOrDownsScale(weekVisitorGain)
                .setMonthVisitor(thisMonthVisitorCount)
                .setMonthUpsOrDownsScale(monthVisitorGain);
    }

    /**
     * 获取某个日期到今天的访客数量
     * @param todayVisitorCount 今日访客数量
     * @param date 日期
     * @return 访客数量
     */
    private Integer getVisitorCountToToday(Integer todayVisitorCount, LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = DateUtil.getLastDay();
        Integer visitorCountExceptToday = 0;
        // 当今天是第一天时查询统计表数据，visitorCountExceptToday=0
        if (date.compareTo(today) != 0) {
            // 开始日期到昨日的访客数量从访客统计表获取
            visitorCountExceptToday = visitorDubboService.getVisitorCountByStatistics(date, yesterday);
            visitorCountExceptToday = visitorCountExceptToday == null ? 0 : visitorCountExceptToday;
        }
        return visitorCountExceptToday + todayVisitorCount;
    }

    /**
     * 获取访客统计数据
     *
     * @param frontPageVisitorQueryDTO
     * @return list
     */
    public List<FrontPageVisitorStatisticsByDayVO> listVisitorStatistics(FrontPageVisitorQueryDTO frontPageVisitorQueryDTO) {
        LocalDate today = LocalDate.now();
        LocalDate beginDate = frontPageVisitorQueryDTO.getBeginDate();
        LocalDate endDate = frontPageVisitorQueryDTO.getEndDate();
        List<VisitorStatisticsByDayDO> list = visitorDubboService.listVisitorStatisticsByDay(beginDate, endDate);
        // 如果结束时间为今天或者今天之后，需要查询今日访客数据
        if (today.compareTo(endDate) <= 0) {
            Integer todayVisitorCount = visitorDubboService.getVisitorCount(today);
            for (VisitorStatisticsByDayDO statistics : list) {
                if (today.compareTo(statistics.getRecordDate()) == 0) {
                    statistics.setCount(todayVisitorCount);
                }
            }
        }
        return frontPageConvert.toVisitorDayVO(list);
    }
}
