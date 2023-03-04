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

/**
 * 系统角色表(Role)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:12
 */
@Data
@Accessors(chain = true)
public class RoleVO implements Serializable {

    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色描述")
    private String roleDesc;

    @ApiModelProperty("角色所属系统")
    private Long systemId;

    @ApiModelProperty("系统名称")
    @TransformService(field = "systemName", service = SystemService.class)
//    @Transform(transformer = ServiceImplTransformer.class, dataSource = SystemService.class, from = "systemId", extra = "systemName")
    private String systemName;

    @ApiModelProperty("是否开启")
    private Boolean isEnable;

    @ApiModelProperty("创建人id")
    private Long createUser;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人名称")
    @Transform(transformer = CreateUpdateUserTransformer.class, from = "createUser")
    private Long createUserName;
}

