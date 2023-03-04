package com.msb.user.core.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (UserLoginRecord)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Data
public class UserLoginRecordDTO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("登录下放token")
    private String token;

    @ApiModelProperty("登录来源 待定义 1学习平台2严选商城3某某后管  登录哪个系统")
    private Integer source;

    @ApiModelProperty("ip")
    private String ip;

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

