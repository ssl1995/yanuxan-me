package com.msb.mall.marketing.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 商品推荐表(ProductRecommended)表实体类
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Data
@Accessors(chain = true)
public class ProductRecommendedEnableDTO implements Serializable {

    @NotNull
    @ApiModelProperty("推荐商品id")
    private Long id;
    @NotNull
    @ApiModelProperty("是否开启")
    private Boolean isEnable;
}

