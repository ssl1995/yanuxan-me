package com.msb.im.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.model.entity.SessionUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * (HorseImSessionUser)表数据库访问层
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:34
 */
public interface SessionUserMapper extends BaseMapper<SessionUser> {

    /**
     * 查询会话包含的用户  包含把会话删除了的用户
     *
     * @param sessionId
     * @return
     */
    @Select("select user_id from session_user where session_id = #{sessionId}")
    Set<String> findUserIdBySessionId(@Param("sessionId") Long sessionId);

    @Update("update session_user set un_read_count = #{unreadCount}, update_user = #{updateUser}, update_time_stamp = #{updateTimeStamp} where is_deleted = 0 and session_id = #{sessionId} and user_id = #{userId}")
    void updateUnreadCount(@Param("sessionId") Long sessionId, @Param("userId") String userId, @Param("unreadCount") Long unreadCount, @Param("updateTimeStamp") Long updateTimeStamp, @Param("updateUser") String updateUser);

    @Select("select max(id) from session_user")
    Long findMaxId();

    void updateByCondition(@Param("sessionUser") SessionUser sessionUser, @Param("condition") SessionUser condition, @Param("isAddUnread") boolean isAddUnread);

    List<SessionUser> findBySysIdAndUserId(@Param("sysId") Integer sysId, @Param("userId") String userId, @Param("types") List<Integer> types);
}

