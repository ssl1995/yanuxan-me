package com.msb.like.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class LikesQueryDO implements Serializable {
    @ApiModelProperty("系统id")
    private Long systemId;

    @ApiModelProperty("场景id")
    private Long scenesId;

    @ApiModelProperty("业务id 查询单条点赞数据时使用")
    private Long businessId;

    @ApiModelProperty("业务id集合 查询多条点赞数据时使用")
    private List<Long> businessIdList;

    @ApiModelProperty("用户id 查询单条点赞数据时使用")
    private Long userId;

    @ApiModelProperty("用户id集合 查询多条点赞数据时使用")
    private List<Long> userIdList;
}
