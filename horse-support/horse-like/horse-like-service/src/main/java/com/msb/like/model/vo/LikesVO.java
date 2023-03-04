package com.msb.like.model.vo;



import lombok.Data;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;


/**
 * 点赞表(Like)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-22 20:30:58
 */
@Data
public class LikesVO implements Serializable {

    @ApiModelProperty("点赞id")
    private Long id;
    
    @ApiModelProperty("系统id")
    private Long systemId;
    
    @ApiModelProperty("场景id")
    private Long scenesId;
    
    @ApiModelProperty("业务id")
    private Long businessId;
    
    @ApiModelProperty("用户id")
    private Long userId;
}

