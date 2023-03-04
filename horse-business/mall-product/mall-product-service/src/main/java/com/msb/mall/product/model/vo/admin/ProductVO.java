package com.msb.mall.product.model.vo.admin;


import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.product.enums.ProductTypeEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 商品(Product)表实体类
 *
 * @author luozhan
 * @date 2022-03-30 18:04:03
 */
@Data
public class ProductVO implements Serializable {

    @ApiModelProperty("商品编号")
    private Long id;

    @ApiModelProperty("商品名称")
    private String name;

    @ApiModelProperty("商品分类ID")
    private Long categoryId;

    @ApiModelProperty("起售价")
    private BigDecimal startingPrice;

    @ApiModelProperty("总库存")
    private BigDecimal totalStock;

    @ApiModelProperty("商品主图")
    private String mainPicture;

    @ApiModelProperty("偏远地区邮费")
    private BigDecimal remoteAreaPostage;

    @ApiModelProperty("单次购买上限")
    private Integer singleBuyLimit;

    @ApiModelProperty("上下架状态:1-上架,0-下架")
    private Boolean isEnable;

    @ApiModelProperty("商品备注")
    private String remark;

    @ApiModelProperty("商品类型")
    @ApiModelPropertyEnum(dictEnum = ProductTypeEnums.class)
    private Integer productType;


}

