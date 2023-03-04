package com.msb.mall.comment.model.vo.admin;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;


/**
 * 商家评论(MerchantComment)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-20 16:14:38
 */
@Data
public class MerchantCommentVO implements Serializable {

    @ApiModelProperty("id")
    private Long id;
    
    @ApiModelProperty("来源id")
    private Long originId;
    
    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String userName;
    
    @ApiModelProperty("评论内容")
    private String commentContent;
    
    @ApiModelProperty("是否显示（0-否 1-是）")
    private Boolean isShow;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    
}

