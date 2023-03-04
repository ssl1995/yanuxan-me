package com.msb.mall.trade.model.dto.app;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.api.enums.CommentStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("APP订单评论状态查询DTO")
public class OrderCommentStatusDTO extends PageDTO implements Serializable {
    @ApiModelProperty("评论状态")
    @ApiModelPropertyEnum(dictEnum = CommentStatusEnum.class)
    @NotNull
    private Integer commentStatus;
}
