package com.msb.mall.product.model.vo.app;


import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.product.enums.ProductTypeEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 商品详情页VO
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Accessors(chain = true)
@Data
@ApiModel("商品详情页VO")
public class ProductDetailVO implements Serializable {

    @ApiModelProperty("商品ID")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @ApiModelProperty("商品图片列表")
    private List<String> pictureList;

    @ApiModelProperty("起售价")
    private BigDecimal startingPrice;

    @ApiModelProperty("商品详情")
    private String detail;

    @ApiModelProperty("商品属性列表")
    private List<ProductAttributeGroupSimpleVO> attributeGroupList;

    @ApiModelProperty("偏远地区邮费")
    private BigDecimal remoteAreaPostage;

    @ApiModelProperty("单次购买上限")
    private Integer singleBuyLimit;

    @ApiModelProperty("是否上架")
    private Boolean isEnable;

    @ApiModelProperty("活动商品信息")
    private ProductActivityVO productActivityVO;

    @ApiModelProperty("商品类型")
    @ApiModelPropertyEnum(dictEnum = ProductTypeEnums.class)
    private Integer productType;
}

