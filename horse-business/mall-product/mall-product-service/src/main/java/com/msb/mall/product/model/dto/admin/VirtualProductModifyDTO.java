package com.msb.mall.product.model.dto.admin;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * 虚拟商品表(VirtualProduct)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-25 10:13:39
 */
@Data
public class VirtualProductModifyDTO implements Serializable {

    @NotNull
    @ApiModelProperty("发货类型（1-文件 2-消息）")
    private Integer shipType;

    @Size(max = 255)
    @NotNull
    @ApiModelProperty("发货内容")
    private String shipContent;
    
}

