package com.msb.mall.product.model.vo.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 商品属性(ProductAttribute)表实体类
 *
 * @author luozhan
 * @date 2022-03-29 14:37:39
 */
@Data
public class ProductAttributeVO implements Serializable {

    @ApiModelProperty("属性ID")
    private Long id;

    @ApiModelProperty("属性名")
    private String name;

    @ApiModelProperty("商品属性组ID")
    private Long productAttributeGroupId;

    @ApiModelProperty("商品ID")
    private Long productId;

    @ApiModelProperty("标记值，同一个商品下唯一，用于检索属性")
    private Integer symbol;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新人id")
    private Long updateUser;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}

