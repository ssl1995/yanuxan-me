package com.msb.user.core.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统角色表(Role)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Data
@Accessors(chain = true)
public class RoleQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色所属系统")
    private Long systemId;

    @ApiModelProperty("是否开启")
    private Boolean isEnable;
}

