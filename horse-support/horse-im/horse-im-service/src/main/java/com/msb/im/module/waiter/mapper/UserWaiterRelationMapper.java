package com.msb.im.module.waiter.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.module.waiter.model.bo.UserWaiterBo;
import com.msb.im.module.waiter.model.entity.UserWaiterRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商铺和客服的关联
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
public interface UserWaiterRelationMapper extends BaseMapper<UserWaiterRelation> {
    /**
     * 查询商铺和用户关联的sessionId
     *
     * @param storeId
     * @param userId
     * @return
     */
    Long findSessionId(@Param("storeId") Long storeId, @Param("userId") String userId);
}

