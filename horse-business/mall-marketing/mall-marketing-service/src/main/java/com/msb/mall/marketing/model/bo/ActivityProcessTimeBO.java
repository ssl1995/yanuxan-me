package com.msb.mall.marketing.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ActivityProcessTimeBO {

    private Long activityTimeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ActivityProductBO activityProductBO;
}
