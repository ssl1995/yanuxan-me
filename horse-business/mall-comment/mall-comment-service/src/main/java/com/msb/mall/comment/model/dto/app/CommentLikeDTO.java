package com.msb.mall.comment.model.dto.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CommentLikeDTO implements Serializable {
    @ApiModelProperty("评论id")
    @NotNull
    private Long commentId;

    @ApiModelProperty("是否点赞 true-点赞 false-取消点赞")
    @NotNull
    private Boolean isLike;
}
