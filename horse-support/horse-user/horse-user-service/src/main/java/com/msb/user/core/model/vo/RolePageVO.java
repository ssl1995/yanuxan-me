package com.msb.user.core.model.vo;


import com.msb.framework.web.transform.annotation.Transform;
import com.msb.user.core.service.SystemService;
import com.msb.user.core.translate.CreateUpdateUserTransformer;
import com.msb.user.core.translate.TransformService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统角色表(Role)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Data
@Accessors(chain = true)
public class RolePageVO implements Serializable {

    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    @ApiModelProperty("角色所属系统")
    private Long systemId;

    @ApiModelProperty("系统名称")
    @TransformService(service = SystemService.class, field = "systemName")
    private String systemName;

    @ApiModelProperty("是否开启")
    private Boolean isEnable;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @Transform(transformer = CreateUpdateUserTransformer.class, from = "createUser")
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("部门")
    private List<DepartmentVO> departmentList;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}

