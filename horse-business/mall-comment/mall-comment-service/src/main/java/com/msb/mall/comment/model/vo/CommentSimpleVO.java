package com.msb.mall.comment.model.vo;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.comment.enums.CommentTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 86151
 */
@Data
@Accessors(chain = true)
public class CommentSimpleVO implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("originId如果是评论则为0,追评和回复则为评论的id")
    private Long originId;

    @ApiModelProperty("parentId如果是评论则为0,追评则为评论的id,回复则为回复的对象的id")
    private Long parentId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("评论内容")
    private String commentContent;

    @ApiModelProperty("评论分值")
    private String commentScore;

    @ApiModelProperty("评论图片")
    private String pictureUrl;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("评论类型")
    @ApiModelPropertyEnum(dictEnum = CommentTypeEnum.class)
    private Integer commentType;

    @ApiModelProperty("是否包含敏感词")
    private Boolean isContainSensitiveWords;

}
