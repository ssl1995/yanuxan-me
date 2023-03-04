package com.msb.mall.product.model.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author shumengjiao
 */
@Data
@Accessors(chain = true)
public class ProductSkuCommentVO implements Serializable {
    @ApiModelProperty("skuId")
    private Long skuId;

    @ApiModelProperty("属性值列表，关联属性表中的symbol，用逗号分隔，如:“1,3,4”")
    private String attributeSymbolList;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("评论数量")
    private Integer commentCount;
}
