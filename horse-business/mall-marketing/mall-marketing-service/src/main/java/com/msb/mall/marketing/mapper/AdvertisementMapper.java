package com.msb.mall.marketing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.marketing.model.entity.Advertisement;
import org.apache.ibatis.annotations.Param;

/**
 * 广告(Advertisement)表数据库访问层
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
public interface AdvertisementMapper extends BaseMapper<Advertisement> {

    /**
     * 更新排序
     * 如果位置往后调，则新老位置之间的元素的sort全部减1
     * 反之如果位置往前调，则新老位置之间的元素的sort全部加1
     * @param advertisement 广告信息
     * @param oldSort 旧排序
     * @param newSort 新排序
     * @return 更新数量
     */
    Integer updateSort(@Param("advertisement") Advertisement advertisement, @Param("oldSort") int oldSort, @Param("newSort") int newSort);

    /**
     * 查询最小的sort
     * @param advertisement 广告信息
     * @return 最小的sort
     */
    Integer selectMinSort(@Param("advertisement") Advertisement advertisement);
}

