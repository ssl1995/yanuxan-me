package com.msb.mall.comment.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.comment.enums.CommentTypeEnum;
import com.msb.mall.comment.model.vo.admin.MerchantCommentVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * (Comment)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-13 21:24:20
 */
@Data
@Accessors(chain = true)
public class CommentDetailVO implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("originId如果是评论则为0,追评和回复则为评论的id")
    private Long originId;

    @ApiModelProperty("parentId如果是评论则为0,追评则为评论的id,回复则为回复的对象的id")
    private Long parentId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("是否显示（0-否 1-是）")
    private Boolean isShow;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("评论类型")
    @ApiModelPropertyEnum(dictEnum = CommentTypeEnum.class)
    private Integer commentType;

    @ApiModelProperty("用户头像")
    private String userAvatar;

    @ApiModelProperty("规格")
    private String skuName;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品主图")
    private String productPicture;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("图片地址")
    private String pictureUrl;

    @ApiModelProperty("评价分值（1-5星）")
    private Integer commentScore;

    @ApiModelProperty("追评")
    private CommentFollowVO followComment;

    @ApiModelProperty("回复列表")
    private List<CommentAnswerVO> answerCommentList;

    @ApiModelProperty("商家评论")
    private MerchantCommentVO merchantComment;

    @ApiModelProperty("订单详情id")
    private Long orderProductId;

    @ApiModelProperty("有用数")
    private Integer usefulCount;

    @ApiModelProperty("是否点赞")
    private Boolean isLike;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("回复数量（商家回复、其他回复）")
    private Integer replyCount;

}
