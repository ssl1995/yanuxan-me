package com.msb.user.core.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
public class VerificationCodeLoginDTO extends LoginDTO {
    @NotNull
    @ApiModelProperty("手机号")
    private String phone;
    @NotNull
    @ApiModelProperty("验证码")
    private String verificationCode;
}
