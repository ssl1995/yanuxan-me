package com.msb.user.core.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 系统角色权限表(RolePermission)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Data
@Accessors(chain = true)
public class RoleMenuPermissionRelationVO implements Serializable {

    @ApiModelProperty("角色信息")
    private RoleVO role;

    @ApiModelProperty("角色菜单，接口权限信息")
    private List<RoleMenuTreeVO> roleMenuTree;

}

