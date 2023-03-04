package com.msb.mall.im.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 客服用户关联
 *
 * @author shumengjiao
 * @since 2022-06-09 16:21:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("查询客服")
public class WaiterUserDTO extends PageDTO {

    @ApiModelProperty(value = "客服昵称", required = false)
    private String waiterNickname;
}

