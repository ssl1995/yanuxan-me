package com.msb.user.core.model.dto;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.user.core.model.enums.EmployeeToDepartmentEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class EmployeeToDepartmentRoleDTO {

    @ApiModelProperty("员工id")
    private Long employeeId;
    @ApiModelProperty("部门id")
    private Long departmentId;
    @ApiModelPropertyEnum(dictEnum = EmployeeToDepartmentEnum.class)
    @ApiModelProperty("清除权限")
    private Integer handType;
}
