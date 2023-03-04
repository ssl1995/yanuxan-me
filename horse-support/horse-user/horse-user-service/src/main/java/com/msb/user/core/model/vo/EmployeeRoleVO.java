package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 系统角色权限表(EmployeeRole)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Data
@Accessors(chain = true)
public class EmployeeRoleVO implements Serializable {

    private Long id;

    @ApiModelProperty("员工Id")
    private Long employeeId;

    @ApiModelProperty("角色Id")
    private Long roleId;

    @ApiModelProperty("系统id")
    private Long systemId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Boolean isDeleted;

}

