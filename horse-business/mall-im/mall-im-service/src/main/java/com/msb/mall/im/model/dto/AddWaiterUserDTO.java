package com.msb.mall.im.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 客服用户关联
 *
 * @author shumengjiao
 * @since 2022-06-09 16:21:51
 */
@Data
@ApiModel("新增客服")
public class AddWaiterUserDTO {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id为空")
    private Long userId;
    @ApiModelProperty(value = "客服头像", required = true)
    @NotEmpty(message = "客服头像不能为空")
    private String waiterAvatar;
    @ApiModelProperty(value = "客服昵称", required = true)
    @NotEmpty(message = "客服昵称不能为空")
    private String waiterNickname;
}

