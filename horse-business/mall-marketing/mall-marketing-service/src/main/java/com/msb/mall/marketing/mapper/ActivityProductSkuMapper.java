package com.msb.mall.marketing.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.msb.mall.marketing.model.entity.ActivityProductSku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 活动商品表(ActivityProductSku)表数据库访问层
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
public interface ActivityProductSkuMapper extends BaseMapper<ActivityProductSku> {


    /**
     * 检查并扣减库存
     *
     * @param activityProductSkuId sku-id
     * @param quality              数量
     */
    @Update("update activity_product_sku set stock = stock - #{quality} where id = #{activityProductSkuId} and stock >= #{quality}")
    boolean checkAndReduceStock(@Param("activityProductSkuId") Long activityProductSkuId, @Param("quality") Integer quality);

    /**
     * 增加库存
     *
     * @param activityProductSkuId activityProductSkuId
     * @param quality              数量
     * @return boolean
     */
    @Update("update activity_product_sku set stock = stock + #{quality} where id = #{activityProductSkuId}")
    boolean addStock(@Param("activityProductSkuId") Long activityProductSkuId, @Param("quality") Integer quality);
}

