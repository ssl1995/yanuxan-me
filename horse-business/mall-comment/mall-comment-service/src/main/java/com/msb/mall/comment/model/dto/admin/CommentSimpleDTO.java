package com.msb.mall.comment.model.dto.admin;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author 86151
 */
@Data
public class CommentSimpleDTO implements Serializable {

    @NotNull
    @ApiModelProperty("id集合")
    List<Long> idList;

    @NotNull
    @ApiModelProperty("是否显示 是-true 否-false")
    private Boolean isShow;
}
