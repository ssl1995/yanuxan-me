package com.msb.user.core.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 系统角色表(Role)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Data
@Accessors(chain = true)
public class RoleDTO implements Serializable {

    @NotBlank
    @ApiModelProperty("角色名称")
    private String roleName;

    @NotBlank
    @ApiModelProperty("角色描述")
    private String roleDesc;

    @NotNull
    @ApiModelProperty("角色所属系统")
    private Long systemId;

    @NotNull
    @ApiModelProperty("是否开启")
    private Boolean isEnable;
}

