package com.msb.im.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.model.bo.SessionUserBO;
import com.msb.im.model.entity.Session;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * (HorseImSession)表数据库访问层
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:18
 */
public interface SessionMapper extends BaseMapper<Session> {

    List<SessionUserBO> findMySession(@Param("userId") String userId, @Param("sysId") Integer sysId, @Param("size") Integer size);

    @Select("select max(id) from session")
    Long findMaxId();

    /**
     * 按更新时间倒序查询数据库用户未删除的会话id
     *
     * @param userId
     * @param sysId
     * @param size
     * @return
     */
    List<Long> findUserSessionId(@Param("userId") String userId, @Param("sysId") Integer sysId, @Param("size") Integer size);

    /**
     * 查询数据库用户所有的会话id 包含删除的和未删除的
     *
     * @param userId
     * @param sysId
     * @return
     */
    Set<Long> findUserAllSessionId(@Param("userId") String userId, @Param("sysId") Integer sysId);

    /**
     * 查询数据库用户所有的客服会话id 包含删除的和未删除的
     *
     * @param userId
     * @param sysId
     * @return
     */
    Set<Long> findUserAllStoreSessionId(@Param("userId") String userId, @Param("sysId") Integer sysId);

    /**
     * 按消息id 查询未读的会话
     *
     * @param sessionIds
     * @param storeId
     * @return
     */
    List<Session> findUnreadSession(@Param("sessionIds") Set<Long> sessionIds, @Param("storeId") String storeId);

    /**
     * 指定会话id 指定查询的个数 指定时间内 的会话
     *
     * @param sessionIds
     * @param withinTimeHours
     * @param sessionWithinTimeSize
     * @param storeId
     * @return
     */
    List<Session> findWithinTimes(@Param("sessionIds") Set<Long> sessionIds, @Param("withinTimeHours") int withinTimeHours, @Param("sessionWithinTimeSize") int sessionWithinTimeSize, @Param("storeId") String storeId);

    /**
     * 指定会话id 指定查询的个数 的会话
     *
     * @param userId
     * @param sessionIds
     * @return
     */
    List<Session> findBySessionIdsAndUserId(@Param("userId") String userId, @Param("sessionIds") Set<Long> sessionIds);

    Long findStoreUserSessionId(@Param("sysId") Integer sysId, @Param("storeId") String storeId, @Param("userId") String userId);

    Session findSingleSession(@Param("toId") String toId, @Param("fromId") String fromId, @Param("sysId") Integer sysId);

    /**
     * 查询第三方系统发送消息时接收人和发送人是否有会话id交集
     *
     * @param sysId           系统
     * @param fromId          发送人
     * @param toId            接收人
     * @param sessionTypeEnum 会话类型
     * @param sessionPayload  元数据
     * @return 会话id
     */
    Long findDbSessionId(@Param("sysId") Integer sysId, @Param("fromId") String fromId, @Param("toId") String toId, @Param("sessionTypeEnum") Integer sessionTypeEnum, @Param("sessionPayload") String sessionPayload);
}

