package com.msb.im.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * 会话用户关联VO
 *
 * @author zhoumiao
 * @since 2022-04-13 16:29:34
 */
@Data
public class SessionUserVO implements Serializable {

    private Integer id;

    private Integer sessionId;

    private Integer userId;

}

