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
@ApiModel("更新客服")
public class UpdateWaiterUserDTO {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id为空")
    private Long id;
    @ApiModelProperty(value = "用户id", required = false)
    private Long userId;
    @ApiModelProperty(value = "客服头像", required = false)
    private String waiterAvatar;
    @ApiModelProperty(value = "客服昵称", required = false)
    private String waiterNickname;
}

