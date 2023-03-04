package com.msb.im.module.waiter.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.im.module.waiter.model.entity.StoreConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 店铺配置
 *
 * @author zhoumiao
 * @since 2022-04-13 16:28:56
 */
public interface StoreConfigMapper extends BaseMapper<StoreConfig> {

    @Select("select count(*) > 0 from store_config where sys_id = #{sysId}")
    boolean sysExist(@Param("sysId") Integer sysId);
}

