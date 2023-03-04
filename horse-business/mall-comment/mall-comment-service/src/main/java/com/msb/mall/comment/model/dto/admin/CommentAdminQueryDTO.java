package com.msb.mall.comment.model.dto.admin;

import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * (Comment)表实体类
 *
 * @author 86151
 * @since 2022-06-13 21:08:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentAdminQueryDTO extends PageDTO implements Serializable {

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("用户昵称")
    private String userName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("是否显示 是-true 否-false")
    private Boolean isShow;

    @ApiModelProperty("评分分值(1-5星)")
    private List<Integer> commentScoreList;

    @ApiModelProperty("评分时间-开始")
    private LocalDateTime commentTimeBegin;

    @ApiModelProperty("评分时间-结束")
    private LocalDateTime commentTimeEnd;
}
