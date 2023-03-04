package com.msb.mall.comment.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.comment.enums.CommentLabelEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 86151
 */
@Data
@Accessors(chain = true)
public class CommentLabelVO implements Serializable {

    @ApiModelProperty("标签类型")
    @ApiModelPropertyEnum(dictEnum = CommentLabelEnum.class)
    private Integer labelType;

    @ApiModelProperty("标签名称")
    @TransformEnum(value = CommentLabelEnum.class, from = "labelType")
    private String labelName;

    @ApiModelProperty("评论数量")
    private Integer commentCount;
}
