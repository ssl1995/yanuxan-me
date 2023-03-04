package com.msb.user.api.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Data
public class MenuDO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("父级菜单")
    private Long parentId;

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

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Boolean isDeleted;

}

