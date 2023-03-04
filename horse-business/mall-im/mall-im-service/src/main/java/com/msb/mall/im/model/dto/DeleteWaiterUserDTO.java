package com.msb.mall.im.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


/**
 * 客服用户关联
 *
 * @author shumengjiao
 * @since 2022-06-09 16:21:51
 */
@Data
@ApiModel("删除客服")
public class DeleteWaiterUserDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotEmpty(message = "id不能为空")
    private List<Long> ids;
}

