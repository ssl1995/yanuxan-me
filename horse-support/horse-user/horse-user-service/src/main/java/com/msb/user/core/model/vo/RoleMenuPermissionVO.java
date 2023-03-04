package com.msb.user.core.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统角色权限表(RolePermission)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Data
@Accessors(chain = true)
public class RoleMenuPermissionVO implements Serializable {

    private Long id;

    @ApiModelProperty("角色Id")
    private Long roleId;

    @ApiModelProperty("权限ID")
    private Long permissionId;

    @ApiModelProperty("系统id")
    private Long systemId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Boolean isDeleted;

}

