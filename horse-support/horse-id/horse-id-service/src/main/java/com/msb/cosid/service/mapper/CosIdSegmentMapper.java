package com.msb.cosid.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.cosid.service.model.entity.CosIdSegment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author liao
 */
public interface CosIdSegmentMapper extends BaseMapper<CosIdSegment> {


    /**
     * 自增增加号段
     * @param id 号段id
     * @param segmentNumber 号段数量
     * @return
     */
    @Update("update cosid_segment set auto_increment = auto_increment + #{segmentCount} where id = #{id} ")
    Boolean updateCosIdSegmentAutoIncrement(@Param("id") Long id, @Param("segmentCount") Long segmentCount);
}
