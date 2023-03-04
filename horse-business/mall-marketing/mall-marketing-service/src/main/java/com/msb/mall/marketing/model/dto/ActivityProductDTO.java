package com.msb.mall.marketing.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动商品表(ActivityProduct)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Data
@Accessors(chain = true)
public class ActivityProductDTO implements Serializable {

    @NotNull
    @ApiModelProperty("活动对应时间段id")
    private Long activityTimeId;

    @NotNull
    @ApiModelProperty("商品id")
    private List<Long> productId;

}

