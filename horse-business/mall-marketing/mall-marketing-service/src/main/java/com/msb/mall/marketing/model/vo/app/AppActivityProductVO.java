package com.msb.mall.marketing.model.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AppActivityProductVO {

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("秒杀商品id")
    private Long activityProductId;

    @ApiModelProperty("商品主图")
    private String productMainPicture;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("秒杀价格")
    private BigDecimal activityPrice;

    @ApiModelProperty("价格")
    private BigDecimal originalPrice;

    @ApiModelProperty("库存")
    private Integer stock;

    @ApiModelProperty("秒杀总数量")
    private Integer number;
}
