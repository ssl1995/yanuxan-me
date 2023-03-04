package com.msb.user.core.model.dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 权限表(Permission)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Data
@Accessors(chain = true)
public class PermissionDTO implements Serializable {

    @NotNull
    @ApiModelProperty("系统id")
    private Long systemId;

    @NotBlank
    @ApiModelProperty("接口名称")
    private String name;

    @NotNull
    @ApiModelProperty("所属菜单id")
    private Long menuId;

    @NotNull
    @ApiModelProperty("接口路径")
    private String uri;

    @NotNull
    @ApiModelProperty("http请求类型 GET,POST,PUT,DELETE 等")
    private String method;

    @NotNull
    @ApiModelProperty("权限类型，1，需要分配，2，登录既有的")
    private Integer type;

    @NotNull
    @ApiModelProperty("服务名")
    private String service;

    @NotNull
    @ApiModelProperty("是否开启")
    private Boolean isEnable;
}

