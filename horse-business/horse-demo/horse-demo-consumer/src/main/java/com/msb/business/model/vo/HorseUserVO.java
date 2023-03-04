package com.msb.business.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (HorseUser)表实体类
 *
 * @author makejava
 * @since 2022-03-21 14:57:36
 */
@Data
public class HorseUserVO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("地址")
    private String address;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

    private Boolean isDeleted;

}

