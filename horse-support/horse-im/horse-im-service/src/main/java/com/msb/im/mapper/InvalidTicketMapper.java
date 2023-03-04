package com.msb.im.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.model.entity.InvalidTicket;
import com.msb.im.model.entity.ThirdSystemConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 连接时已经使用过的ticket表实体类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
public interface InvalidTicketMapper extends BaseMapper<InvalidTicket> {

    @Select("select last_timestamp from invalid_ticket where sys_id = #{sysId} and from_id = #{fromId} and type = #{type}")
    Long findLastTimestamp(@Param("sysId")Integer sysId, @Param("fromId")String fromId, @Param("type")String type);
}

