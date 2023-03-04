package com.msb.mall.marketing.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 商品推荐表(ProductRecommended)表实体类
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Data
@Accessors(chain = true)
public class ProductRecommendedVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品主图")
    private String productMainPicture;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("是否开启 1开启，0未开启")
    private Boolean isEnable;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新人id")
    private Long updateUser;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}

