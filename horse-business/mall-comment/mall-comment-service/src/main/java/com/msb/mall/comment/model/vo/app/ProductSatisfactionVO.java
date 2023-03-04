package com.msb.mall.comment.model.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ProductSatisfactionVO implements Serializable {
    @ApiModelProperty("是否计算商品满意度 10条及以下评论不计算")
    private Boolean isCalculateSatisfaction;

    @ApiModelProperty("商品满意度")
    private String productSatisfaction;
}
