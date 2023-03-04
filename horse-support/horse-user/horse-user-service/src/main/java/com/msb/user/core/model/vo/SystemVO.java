package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 系统表(System)表实体类
 *
 * @author makejava
 * @date 2022-04-25 16:56:41
 */
@Data
@Accessors(chain = true)
public class SystemVO implements Serializable {

    @ApiModelProperty("系统id")
    private Long id;

    @ApiModelProperty("系统标识")
    private String systemCode;

    @ApiModelProperty("系统名称")
    private String systemName;

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

