package com.msb.mall.marketing.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 活动商品表(ActivityProduct)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Data
@Accessors(chain = true)
public class ActivityProductQueryDTO extends PageDTO implements Serializable {

    @NotNull
    @ApiModelProperty("秒杀时间段id")
    private Long activityTimeId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品分类id")
    private Long categoryId;
}

