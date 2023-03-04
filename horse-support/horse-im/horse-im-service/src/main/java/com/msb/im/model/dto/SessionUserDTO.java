package com.msb.im.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * (HorseImSessionUser)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:34
 */
@Data
public class SessionUserDTO implements Serializable {

    private Integer id;

    private Integer sessionId;

    private Integer userId;

}

