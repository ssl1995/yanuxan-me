package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 权限表(Permission)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Data
@Accessors(chain = true)
public class PermissionVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("接口名称")
    private String name;

    @ApiModelProperty("所属菜单id")
    private Long menuId;

    @ApiModelProperty("服务名")
    private String service;

    @ApiModelProperty("接口路径")
    private String uri;

    @ApiModelProperty("http请求类型 GET,POST,PUT,DELETE 等")
    private String method;

    @ApiModelProperty("是否开启")
    private Boolean isEnable;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Boolean isDeleted;

}

