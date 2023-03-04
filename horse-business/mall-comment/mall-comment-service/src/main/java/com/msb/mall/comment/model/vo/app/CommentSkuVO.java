package com.msb.mall.comment.model.vo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 86151
 */
@Data
public class CommentSkuVO implements Serializable {
    @ApiModelProperty("属性值列表，关联属性表中的symbol，用逗号分隔，如:“1,3,4”")
    private String attributeSymbolList;

    @ApiModelProperty("评论数量")
    private Integer commentCount;
}
