package com.msb.mall.comment.model.dto.app;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.comment.enums.CommentLabelEnum;
import com.msb.mall.comment.enums.SortTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * app评论查询DTO
 * @author 86151
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CommentAppQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("商品id")
    @NotNull
    private Long productId;

    @ApiModelProperty("排序规则")
    @ApiModelPropertyEnum(dictEnum = SortTypeEnum.class)
    private Integer sortType;

    @ApiModelProperty("是否有内容 是-true 否-false")
    @NotNull
    private Boolean isContent;

    @ApiModelProperty("评论标签")
    @ApiModelPropertyEnum(dictEnum = CommentLabelEnum.class)
    private Integer commentLabel;

}
