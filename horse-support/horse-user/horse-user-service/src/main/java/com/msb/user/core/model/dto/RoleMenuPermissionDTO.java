package com.msb.user.core.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class RoleMenuPermissionDTO {

    @ApiModelProperty("角色id")
    private Long id;

    @ApiModelProperty("拥有的菜单id")
    private Long [] menuIds;

    @ApiModelProperty("拥有的权限id")
    private Long [] permissionIds;
}
