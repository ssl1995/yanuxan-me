package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 员工部门表(EmployeeDepartment)表实体类
 *
 * @author makejava
 * @date 2022-05-09 14:03:23
 */
@Data
@Accessors(chain = true)
public class EmployeeDepartmentVO implements Serializable {

    private Long id;

    @ApiModelProperty("员工Id")
    private Long employeeId;

    @ApiModelProperty("部门id")
    private Long departmentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Boolean isDeleted;

}

