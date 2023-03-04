package com.msb.mall.comment.model.dto.admin;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import com.msb.framework.common.model.PageDTO;

import javax.validation.constraints.NotNull;


/**
 * 商家评论(MerchantComment)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-20 16:14:38
 */
@Data
public class MerchantCommentDTO implements Serializable {
    
    @ApiModelProperty("来源id")
    @NotNull
    private Long originId;
    
    @ApiModelProperty("评论内容")
    @NotNull
    private String commentContent;
    
}

