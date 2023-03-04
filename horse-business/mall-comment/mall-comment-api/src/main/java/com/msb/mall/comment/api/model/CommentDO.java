package com.msb.mall.comment.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CommentDO implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("订单详情id")
    private Long orderProductId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("图片地址")
    private String pictureUrl;

    @ApiModelProperty("有用数")
    private Integer usefulCount;

    @ApiModelProperty("评论类型（1-评论 2-追评 3-回复）")
    private Integer commentType;

    @ApiModelProperty("是否显示（0-否 1-是）")
    private Boolean isShow;

    @ApiModelProperty("是否默认评价（0-否 1-是）")
    private Boolean isDefaultComment;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("评价分值（1-5星）")
    private Integer commentScore;
}
