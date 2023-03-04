package com.msb.user.core.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.user.core.model.entity.VisitorUvStatistics;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

/**
 * 访客统计表(VisitorStatistics)表数据库访问层
 *
 * @author shumengjiao
 * @since 2022-06-07 12:55:10
 */
public interface VisitorUvStatisticsMapper extends BaseMapper<VisitorUvStatistics> {

    @Select("SELECT SUM(count) FROM visitor_uv_statistics WHERE record_date BETWEEN #{beginDate} AND #{endDate}")
    Integer getVisitorCount(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);
}

