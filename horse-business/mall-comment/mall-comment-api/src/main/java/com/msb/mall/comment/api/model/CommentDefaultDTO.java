package com.msb.mall.comment.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class CommentDefaultDTO implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("商品id")
    private Long productId;

    @ApiModelProperty("订单详情id")
    private Long orderProductId;

}
