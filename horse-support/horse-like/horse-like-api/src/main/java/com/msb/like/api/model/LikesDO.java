package com.msb.like.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LikesDO implements Serializable {
    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("场景id")
    private Long scenesId;

    @ApiModelProperty("业务id")
    private Long businessId;

    @ApiModelProperty("用户id")
    private Long userId;
}
