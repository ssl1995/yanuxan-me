package com.msb.mall.marketing.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Accessors(chain = true)
@Data
public class ProductLabelDO implements Serializable {

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品标签")
    private List<ProductLabelEnum> labelList;
}
