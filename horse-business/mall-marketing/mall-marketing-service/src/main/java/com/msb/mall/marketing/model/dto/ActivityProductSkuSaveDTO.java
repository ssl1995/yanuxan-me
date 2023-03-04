package com.msb.mall.marketing.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 活动商品表(ActivityProductSku)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:55
 */
@Data
@Accessors(chain = true)
public class ActivityProductSkuSaveDTO implements Serializable {

    @NotNull
    @ApiModelProperty("活动商品id")
    private Long activityProductId;

    @Valid
    @NotEmpty
    @ApiModelProperty("活动商品sku列表")
    private List<ActivityProductSkuInfoDTO> activityProductSkuInfoList;

    @Data
    @Accessors(chain = true)
    public static class ActivityProductSkuInfoDTO {

        @NotNull
        @ApiModelProperty("商品sku id")
        private Long productSkuId;

        @NotNull
        @ApiModelProperty("原价")
        private BigDecimal originalPrice;

        @NotNull
        @ApiModelProperty("sku活动秒杀价格")
        private BigDecimal price;

        @NotNull
        @ApiModelProperty("活动秒杀数量")
        private Integer number;
    }

}

