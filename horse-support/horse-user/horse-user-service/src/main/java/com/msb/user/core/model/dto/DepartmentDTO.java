package com.msb.user.core.model.dto;


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
public class DepartmentDTO implements Serializable {

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("上级部门ID")
    private Long parentId;

    @ApiModelProperty("0:可用 1：删除")
    private Boolean isEnable;
}

