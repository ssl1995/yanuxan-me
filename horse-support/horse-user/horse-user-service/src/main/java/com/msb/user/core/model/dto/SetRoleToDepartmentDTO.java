package com.msb.user.core.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SetRoleToDepartmentDTO {

    @ApiModelProperty("角色id")
    public Long [] roleIds;
    @ApiModelProperty("部门id")
    public Long departmentId;
    @ApiModelProperty("是否分配给现在部门下的员工")
    public Boolean isDistributionCurrentDepartmentEmployee;
}
