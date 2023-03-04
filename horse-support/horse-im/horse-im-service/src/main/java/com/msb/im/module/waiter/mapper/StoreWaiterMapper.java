package com.msb.im.module.waiter.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.module.waiter.model.entity.StoreWaiter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商铺和客服的关联
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
public interface StoreWaiterMapper extends BaseMapper<StoreWaiter> {

    @Select("select sw.* from store_config sc inner join store_waiter sw on sc.id = sw.store_id where sc.sys_id = #{sysId}")
    List<StoreWaiter> findBySysId(@Param("sysId") int sysId);
}

