package com.msb.mall.marketing.model.bo;

import com.msb.mall.marketing.model.entity.Activity;
import com.msb.mall.marketing.model.entity.ActivityProduct;
import com.msb.mall.marketing.model.entity.ActivityTime;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ActivityProductBO {

    private ActivityProduct activityProduct;
    private ActivityTime activityTime;
    private Activity activity;
}
