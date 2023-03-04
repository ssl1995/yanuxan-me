package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 用户登录限制(UserLoginLimit)表实体类
 *
 * @author makejava
 * @date 2022-04-28 20:49:01
 */
@Data
@Accessors(chain = true)
public class UserLoginLimitVO implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("系统客户端id")
    private Long systemClientId;

    @ApiModelProperty("登录端限制个数")
    private Long loginLimitNum;

    @ApiModelProperty("超出限制后，1，拒绝登录，2，挤掉最老的一个会话")
    private Integer beyondMode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建用户")
    private Long createUser;

    @ApiModelProperty("修改用户")
    private Long updateUser;

    @ApiModelProperty("逻辑删除")
    private Boolean isDeleted;

}

