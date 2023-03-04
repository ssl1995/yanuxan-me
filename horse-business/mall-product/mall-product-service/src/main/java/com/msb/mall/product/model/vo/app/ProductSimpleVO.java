package com.msb.mall.product.model.vo.app;


import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.marketing.api.model.ProductLabelEnum;
import com.msb.mall.product.enums.ProductTypeEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 商品简要信息VO
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
@Accessors(chain = true)
public class ProductSimpleVO implements Serializable {

    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("起售价")
    private BigDecimal startingPrice;

    @ApiModelProperty("商品主图")
    private String mainPicture;

    @ApiModelPropertyEnum(dictEnum = ProductLabelEnum.class)
    @ApiModelProperty("商品标签")
    private List<ProductLabelEnum> labelList;

    @ApiModelProperty(value = "单次购买上限", hidden = true)
    private Integer singleBuyLimit;

    @ApiModelProperty(value = "上下架状态", hidden = true)
    private Boolean isEnable;

    @ApiModelProperty("商品类型")
    @ApiModelPropertyEnum(dictEnum = ProductTypeEnums.class)
    private Integer productType;
}

