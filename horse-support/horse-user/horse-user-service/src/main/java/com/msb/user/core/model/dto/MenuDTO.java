package com.msb.user.core.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (Menu)表实体类
 *
 * @author makejava
 * @since 2022-03-23 20:13:01
 */
@Data
public class MenuDTO implements Serializable {

    @NotNull
    @ApiModelProperty("系统id")
    private Long systemId;

    @NotNull
    @ApiModelProperty("父级菜单")
    private Long parentId;

    @NotBlank
    @ApiModelProperty("菜单标题")
    private String title;

    @NotBlank
    @ApiModelProperty("菜单权限标识")
    private String permission;

    @NotNull
    @ApiModelProperty("菜单类型")
    private Integer type;

    @NotBlank
    @ApiModelProperty("前端页面")
    private String page;

    @NotBlank
    @ApiModelProperty("前端路由")
    private String path;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("激活菜单")
    private String activeMenu;

    @NotBlank
    @ApiModelProperty("排序")
    private Integer sort;
}

