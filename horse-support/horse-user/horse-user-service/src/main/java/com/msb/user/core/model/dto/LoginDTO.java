package com.msb.user.core.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {

    @NotNull
    @ApiModelProperty("登录系统")
    private Integer systemId;
    @NotNull
    @ApiModelProperty("登录来源")
    private Integer clientId;
}
