package com.msb.mall.marketing.api.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@Data
public class ActivityProductDO implements Serializable {

    /**
     * 是否有正在进行的秒杀活动
     */
    private Boolean isInProgress;

    /**
     * 秒杀时间段id
     */
    private Long activityTimeId;

    /**
     * 秒杀活动id
     */
    private Long activityId;

    /**
     * 该商品是否存在秒杀活动
     */
    private Boolean isActivityProduct;

    /**
     * 活动开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime activityEndTime;

    /**
     * 商品sku 信息
     */
    private List<ProductSkuDO> activityProductSkuListDO;

    public static ActivityProductDO buildNotActivity() {
        return new ActivityProductDO().setIsActivityProduct(Boolean.FALSE);
    }
}
