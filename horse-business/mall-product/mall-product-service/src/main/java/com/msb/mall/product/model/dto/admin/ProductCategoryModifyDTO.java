package com.msb.mall.product.model.dto.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * 商品分类查询DTO
 *
 * @author luozhan
 * @date 2022-03-29 18:21:57
 */
@Data
public class ProductCategoryModifyDTO implements Serializable {

    @ApiModelProperty("父分类id（如果是新增、修改一级分类，传0）")
    private Long parentId;

    @NotBlank
    @Size(max = 12)
    @ApiModelProperty("分类名称")
    private String name;

    @Size(max = 255)
    @ApiModelProperty("图片地址")
    private String picture;

    @NotNull
    @ApiModelProperty("是否显示")
    private Boolean isEnable;
}

