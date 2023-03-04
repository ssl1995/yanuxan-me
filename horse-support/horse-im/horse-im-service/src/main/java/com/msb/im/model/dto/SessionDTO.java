package com.msb.im.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * (HorseImSession)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:19
 */
@Data
public class SessionDTO implements Serializable {

    private Integer id;

    @ApiModelProperty("1-严选商城")
    private Integer sysId;

    @ApiModelProperty("1-单聊会话 2-群聊会话")
    private Integer type;

    @ApiModelProperty("群头像")
    private String groupAvatar;

    @ApiModelProperty("群名称")
    private String groupName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}

