package com.msb.like.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LikesModifyDO implements Serializable {
    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("场景id")
    private Long scenesId;

    @ApiModelProperty("业务id")
    private Long businessId;

    @ApiModelProperty("是否点赞 true-点赞 false-取消点赞")
    private Boolean isLike;

    @ApiModelProperty("用户id")
    private Long userId;
}
