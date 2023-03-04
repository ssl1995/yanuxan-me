package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 用户系统关联表(UserSystemRelation)表实体类
 *
 * @author makejava
 * @date 2022-04-25 16:56:40
 */
@Data
@Accessors(chain = true)
public class UserSystemRelationVO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("系统id")
    private Long systemId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;
}

