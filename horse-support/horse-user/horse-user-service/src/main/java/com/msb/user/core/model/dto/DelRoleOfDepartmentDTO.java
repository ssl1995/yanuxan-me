package com.msb.user.core.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelRoleOfDepartmentDTO {

    @NotNull
    @ApiModelProperty("部门id")
    private Long departmentId;

    @NotNull
    @ApiModelProperty("角色id")
    private Long roleId;

    @NotNull
    @ApiModelProperty("是否删除部门下员工的角色")
    private Boolean isDeleteDepartmentOfEmployeeRole;
}
