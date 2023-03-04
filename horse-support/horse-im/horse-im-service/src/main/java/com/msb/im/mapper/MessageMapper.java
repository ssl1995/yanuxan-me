package com.msb.im.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.model.entity.Message;
import com.msb.im.model.vo.CountDaysMessageVO;
import com.msb.im.model.vo.CountHoursMessageVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * (HorseImMessage)表数据库访问层
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 查询某个会话中消息的最大message_index 会话中的消息message_index是连续的
     *
     * @param sessionId 会话id
     * @return 会话中消息的最大message_index
     */
    @Select("select max(message_index) from message where session_id = #{sessionId}")
    Long findSessionMessageMaxId(@Param("sessionId") Long sessionId);

    /**
     * 查询会话中最后一条消息
     *
     * @param sessionId 会话id
     * @return 会话中最后一条消息
     */
    @Select("select * from message where session_id = #{sessionId} order by message_index desc limit 1")
    Message findLastMessage(@Param("sessionId") Long sessionId);

    @Select("select id from message where session_id = #{sessionId} and is_deleted = 0 order by id desc limit #{size}")
    List<Long> findDbSessionMessageIds(@Param("sessionId") Long sessionId, @Param("size") Long size);

    @Select("select id from message where session_id = #{sessionId} and is_deleted = 0 and id < #{lastMessageId} order by id desc limit #{size}")
    List<Long> findSessionMessageIdsByLastMessageId(@Param("sessionId") Long sessionId, @Param("size") Long selectSize, @Param("lastMessageId") Long lastMessageId);

    List<Message> findDbBySessionIdAndMessageIndexIds(@Param("sessionId") Long sessionId, @Param("messageIndexIds") List<Long> messageIndexIds);

    @Select("select max(id) from message")
    Long findMaxId();

    @Select("select message.create_time_stamp from message inner join session on message.session_id = session.id where message.create_time_stamp >= #{start} and message.create_time_stamp <= #{end} and sys_id = #{systemId}")
    List<Message> findBySysAndTimeBetween(@Param("systemId") Integer systemId, @Param("start") Long start, @Param("end") Long end);

    /**
     * 分小时查询消息数量
     *
     * @param systemId 系统
     * @param days     某一天
     * @return 分小时消息数量
     */
    List<CountHoursMessageVO> groupHoursByDaysMessage(@Param("systemId") Integer systemId, @Param("days") Date days);

    /**
     * 分天数统计消息量
     *
     * @param systemId 系统
     * @param start    开始时间戳
     * @param end      结束时间戳
     * @return 分天数统计消息量
     */
    List<CountDaysMessageVO> groupDaysMessage(@Param("systemId") Integer systemId, @Param("start") Long start, @Param("end") Long end);
}

