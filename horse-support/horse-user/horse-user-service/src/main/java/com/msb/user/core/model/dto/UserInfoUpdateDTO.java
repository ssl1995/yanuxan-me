package com.msb.user.core.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoUpdateDTO {

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("性别 1：男 2 女 3 未知")
    private Integer gender;
}
