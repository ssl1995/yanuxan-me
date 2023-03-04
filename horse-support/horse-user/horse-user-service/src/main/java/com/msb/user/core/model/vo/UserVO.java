package com.msb.user.core.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:02
 */
@Data
public class UserVO implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别 1：男 2 女")
    private Integer gender;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("证件号码")
    private String idCard;

    @ApiModelProperty("用户注册来源 待定义")
    private Integer source;

    @ApiModelProperty("unionid")
    private String unionId;

    @ApiModelProperty("0 可用  1不可用")
    private Boolean isEnable;

    @ApiModelProperty("最后一次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建用户")
    private Long createUser;

    @ApiModelProperty("修改用户")
    private Long updateUser;
}

