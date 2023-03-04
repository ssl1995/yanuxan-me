package com.msb.mall.marketing.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品推荐表(ProductRecommended)表实体类
 *
 * @author makejava
 * @date 2022-04-13 15:44:17
 */
@Data
@Accessors(chain = true)
public class ProductRecommendedQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("是否开启 1开启，0未开启")
    private Boolean isEnable;
}

