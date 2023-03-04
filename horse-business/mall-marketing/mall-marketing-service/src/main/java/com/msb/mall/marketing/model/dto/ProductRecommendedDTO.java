package com.msb.mall.marketing.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 商品推荐表(ProductRecommended)表实体类
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Data
@Accessors(chain = true)
public class ProductRecommendedDTO implements Serializable {

    @NotEmpty
    @ApiModelProperty("商品id")
    private List<Long> productIdList;
}

