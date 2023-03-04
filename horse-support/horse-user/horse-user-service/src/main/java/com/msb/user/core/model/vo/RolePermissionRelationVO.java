package com.msb.user.core.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限表(Permission)表实体类
 *
 * @author makejava
 * @date 2022-05-09 10:18:11
 */
@Data
@Accessors(chain = true)
public class RolePermissionRelationVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("是否拥有")
    private Boolean isHave;

    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("所属菜单id")
    private Long menuId;

    @ApiModelProperty("接口路径")
    private String uri;

    @ApiModelProperty("http请求类型 GET,POST,PUT,DELETE 等")
    private String method;

    @ApiModelProperty("是否开启")
    private Boolean isEnable;

}

