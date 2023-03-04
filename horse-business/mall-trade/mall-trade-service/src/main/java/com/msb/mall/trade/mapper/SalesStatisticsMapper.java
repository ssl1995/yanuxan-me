package com.msb.mall.trade.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.trade.model.entity.SalesStatistics;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 销售额统计表(SalesStatistics)表数据库访问层
 *
 * @author shumengjiao
 * @since 2022-05-30 20:30:19
 */
public interface SalesStatisticsMapper extends BaseMapper<SalesStatistics> {

    @Select("SELECT SUM(sales) FROM sales_statistics WHERE record_date BETWEEN #{beginDate} AND #{endDate}")
    BigDecimal getSales(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

    List<SalesStatistics> listSalesStatisticsByDay(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);
}

