package com.msb.user.core.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.user.core.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询新增用户数量
     * @return 新增用户数量
     */
    @Select("SELECT COUNT(*) FROM user_system_relation WHERE system_id = 1 AND create_time >= #{beginDateTime} AND create_time <= #{endDateTime}")
    Integer getAddUserCount(@Param("beginDateTime") LocalDateTime beginDateTime, @Param("endDateTime") LocalDateTime endDateTime);


    /**
     * 查询用户总数量
     * @return 用户总数量
     */
    @Select("SELECT COUNT(*) FROM user_system_relation WHERE system_id = 1")
    Integer getAllUserCount();
}

