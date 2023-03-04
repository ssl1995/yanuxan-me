package com.msb.mall.marketing.api;

import com.msb.mall.marketing.api.model.ActivityProductDO;
import com.msb.mall.marketing.api.model.ActivityProductSkuDO;
import com.msb.mall.marketing.api.model.ActivityProductSkuDTO;
import com.msb.mall.marketing.api.model.ActivityProductSkuReturnStockDTO;

import java.util.List;

public interface ActivityDubboService {
    /**
     * 秒杀商品sku减库存
     *
     * @param activityProductSkuListDTO 需要减库存的sku
     */
    void checkAndReduceActivityStock(List<ActivityProductSkuDTO> activityProductSkuListDTO);

    /**
     * 根据商品id获取正在秒杀或者即将秒杀的 活动商品信息
     *
     * @param productId 商品id
     * @return  活动商品信息
     */
    ActivityProductDO getActivityProductDO(Long productId);

    /**
     * 获取活动商品sku信息
     *
     * @param activityId     活动id
     * @param activityTimeId 活动时间段id
     * @param productId      商品id
     * @return 活动商品sku信息
     */
    ActivityProductSkuDO getActivityProductSku(Long activityId, Long activityTimeId, Long productId, Long skuId);


    /**
     * 批量返还商品库存
     *
     * @param returnStockDTOList：返还库存DTO列表
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    void batchReturnStock(List<ActivityProductSkuReturnStockDTO> returnStockDTOList);

    /**
     * 返还商品库存
     *
     * @param returnStockDTO：返还库存DTO
     * @return void
     * @author peng.xy
     * @date 2022/5/16
     */
    void returnStock(ActivityProductSkuReturnStockDTO returnStockDTO);
}
