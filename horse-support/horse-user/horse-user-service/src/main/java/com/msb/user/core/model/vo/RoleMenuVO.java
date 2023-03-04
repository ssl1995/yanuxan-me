package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 系统角色菜单表(RoleMenu)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Data
@Accessors(chain = true)
public class RoleMenuVO implements Serializable {

    private Long id;

    @ApiModelProperty("角色Id")
    private Long roleId;

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("系统id")
    private Long systemId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Boolean isDeleted;

}

