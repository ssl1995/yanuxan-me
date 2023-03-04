package com.msb.user.core.model.vo;

import com.msb.framework.common.model.UserLoginInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginSuccessVO {

    @ApiModelProperty("用户基本信息")
    private UserLoginInfo user;

    @ApiModelProperty("token")
    private String token;
}
