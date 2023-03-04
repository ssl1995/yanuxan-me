package com.msb.user.core.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PasswordLoginTobDTO extends LoginDTO {
    @NotNull
    @ApiModelProperty("手机号")
    private String phone;
    @NotNull
    @ApiModelProperty("密码")
    private String password;
}
