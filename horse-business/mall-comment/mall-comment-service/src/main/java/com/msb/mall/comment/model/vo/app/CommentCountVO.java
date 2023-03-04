package com.msb.mall.comment.model.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author shumengjiao
 */
@Data
@Accessors(chain = true)
public class CommentCountVO implements Serializable {

    @ApiModelProperty("全部评论数量")
    private Integer allCommentCount;

    @ApiModelProperty("默认评论数量")
    private Integer defaultCommentCount;
}
