package com.msb.user.core.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 部门角色表(DepartmentRole)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:10
 */
@Data
@Accessors(chain = true)
public class DepartmentRoleDTO implements Serializable {

    private Long id;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("部门ID")
    private Long departmentId;

    @ApiModelProperty("系统ID")
    private Long systemId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建用户")
    private Long createUser;

    @ApiModelProperty("修改用户")
    private Long updateUser;

}

