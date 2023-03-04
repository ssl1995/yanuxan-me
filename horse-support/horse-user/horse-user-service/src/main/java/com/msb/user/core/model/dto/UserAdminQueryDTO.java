package com.msb.user.core.model.dto;

import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author liao
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAdminQueryDTO extends PageDTO {

    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("注册时间范围查询 开始")
    private LocalDateTime registeredStartTime;
    @ApiModelProperty("注册时间范围查询 结束")
    private LocalDateTime registeredEndTime;
}
