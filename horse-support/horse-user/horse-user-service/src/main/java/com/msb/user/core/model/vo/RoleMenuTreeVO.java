package com.msb.user.core.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Data
public class RoleMenuTreeVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("是否拥有")
    private Boolean isHave;

    @ApiModelProperty("父级菜单")
    private Long parentId;

    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("菜单标题")
    private String title;

    @ApiModelProperty("菜单权限标识")
    private String permission;

    @ApiModelProperty("菜单类型")
    private Integer type;

    @ApiModelProperty("前端页面")
    private String page;

    @ApiModelProperty("前端路由")
    private String path;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("子菜单")
    private List<RoleMenuTreeVO> menuChild;

    @ApiModelProperty("子权限")
    private List<RolePermissionRelationVO> permissionChild;
}

