package com.msb.user.core.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class SystemGroupRoleVO {

    @ApiModelProperty("系统信息")
    public SystemVO systemVO;

    @ApiModelProperty("角色vo")
    public List<RoleVO> roleListVO;
}
