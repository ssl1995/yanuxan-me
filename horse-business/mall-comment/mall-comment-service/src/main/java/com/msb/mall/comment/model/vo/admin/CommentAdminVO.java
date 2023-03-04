package com.msb.mall.comment.model.vo.admin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (Comment)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-13 21:24:20
 */
@Data
public class CommentAdminVO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("originId如果是评论则为0,追评和回复则为评论的id")
    private Long originId;

    @ApiModelProperty("parentId如果是评论则为0,追评则为评论的id,回复则为回复的对象的id")
    private Long parentId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("图片地址")
    private String pictureUrl;

    @ApiModelProperty("是否显示（0-否 1-是）")
    private Boolean isShow;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("评价分值（1-5星）")
    private Integer commentScore;

}

