package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 部门表(Department)表实体类
 *
 * @author makejava
 * @date 2022-05-09 11:16:28
 */
@Data
@Accessors(chain = true)
public class DepartmentVO implements Serializable {

    private Long id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门链")
    private String chainId;

    @ApiModelProperty("上级部门ID")
    private Long parentId;

    @ApiModelProperty("0:可用 1：删除")
    private Boolean isEnable;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建用户")
    private Long createUser;

    @ApiModelProperty("修改用户")
    private Long updateUser;
}

