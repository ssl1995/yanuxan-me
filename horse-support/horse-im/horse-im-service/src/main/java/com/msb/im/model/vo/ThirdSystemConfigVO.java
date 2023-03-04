package com.msb.im.model.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * 系统配置VO
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Data
@ApiModel("系统")
public class ThirdSystemConfigVO implements Serializable {

    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("系统标识")
    private String client;
    @ApiModelProperty("系统名称")
    private String name;

}

