package com.msb.third.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("微信授权认证VO")
public class WxOAuth2UserInfoVO {

    @ApiModelProperty("微信openId")
    private String openId;

}
