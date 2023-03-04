package com.msb.im.module.waiter.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author zhou miao
 * @date 2022/05/10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("客服VO")
public class WaiterVO {
    @ApiModelProperty("客服的用户账号")
    private String account;
    @ApiModelProperty("客服的用户昵称")
    private String nickname;
    @ApiModelProperty("客服的员工昵称")
    private String waiterNickname;
    @ApiModelProperty("客服的用户id")
    private String userId;
    @ApiModelProperty("客服是否开启")
    private Boolean isEnable;
}
