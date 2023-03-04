package com.msb.user.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ListUserDTO implements Serializable {

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;

}
