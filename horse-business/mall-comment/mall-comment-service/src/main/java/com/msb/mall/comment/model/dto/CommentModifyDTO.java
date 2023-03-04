package com.msb.mall.comment.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * (Comment)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-13 21:24:20
 */
@Data
public class CommentModifyDTO implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("originId评论可以不传，追评和回复必传如果是评论则为0,追评和回复则为评论的id")
    private Long originId;

    @ApiModelProperty("parentId评论可以不传，追评和回复必传如果是评论则为0,追评则为评论的id,回复则为回复的对象的id")
    private Long parentId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("订单详情id 发表评论和追评必传")
    private Long orderProductId;

    @ApiModelProperty("评论内容")
    @NotNull
    private String commentContent;

    @ApiModelProperty("图片地址用,分隔")
    private String pictureUrl;

    @ApiModelProperty("有用数")
    private Integer usefulCount;

    @ApiModelProperty("评论类型（1-评论 2-追评 3-回复）")
    @NotNull
    private Integer commentType;

    @ApiModelProperty("是否显示（0-否 1-是）")
    private Boolean isShow;

    @ApiModelProperty("是否默认评价（0-否 1-是）")
    private Boolean isDefaultComment;

    @ApiModelProperty("是否删除（0-否 1-是）")
    private Boolean isDeleted;

    @ApiModelProperty("评价分值（1-5星）")
    private Integer commentScore;

}

