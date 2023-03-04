package com.msb.mall.im.model.vo;


import com.msb.framework.mysql.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 客服用户关联
 *
 * @author shumengjiao
 * @since 2022-06-09 16:21:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("客服用户关联VO")
public class WaiterUserVO extends BaseEntity implements Serializable {

    private Long id;

    /**
     * 客服id uuid
     */
    @ApiModelProperty("客服id")
    private String waiterId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
    @ApiModelProperty("客服头像")
    private String waiterAvatar;
    @ApiModelProperty("客服昵称")
    private String waiterNickname;
}

