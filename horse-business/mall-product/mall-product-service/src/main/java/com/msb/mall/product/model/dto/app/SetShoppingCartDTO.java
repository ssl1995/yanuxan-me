package com.msb.mall.product.model.dto.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SetShoppingCartDTO {

    @NotNull
    @ApiModelProperty("购物车id")
    private Long id;

    @Min(1)
    @NotNull
    @ApiModelProperty("设置数量")
    private Integer number;
}
