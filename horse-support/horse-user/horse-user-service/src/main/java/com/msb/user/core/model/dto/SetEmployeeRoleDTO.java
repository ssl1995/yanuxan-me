package com.msb.user.core.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SetEmployeeRoleDTO {

    @ApiModelProperty("角色id")
    private Long [] roleIds;
}
