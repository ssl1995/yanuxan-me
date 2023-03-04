package com.msb.user.core.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 系统客户端表(SystemClient)表实体类
 *
 * @author makejava
 * @date 2022-04-25 16:56:40
 */
@Data
@Accessors(chain = true)
public class SystemClientVO implements Serializable {

    @ApiModelProperty("客户端id")
    private Long id;

    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("客户端标识")
    private String clientCode;

    @ApiModelProperty("客户端名称")
    private String clientName;

    @ApiModelProperty("0 不可用  1可用")
    private Boolean isEnable;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建用户")
    private Long createUser;

    @ApiModelProperty("修改用户")
    private Long updateUser;

}

