package com.msb.mall.marketing.model.dto;


import com.msb.framework.common.model.PageDTO;
import lombok.Data;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.Accessors;

/**
 * 活动表(Activity)表实体类
 *
 * @author makejava
 * @date 2022-04-08 13:38:54
 */
@Data
@Accessors(chain = true)
public class PageActivityDTO extends PageDTO implements Serializable {

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("活动状态 0，全部， 1,未开始，2，进行中，3，已结束")
    private Integer activityStatus;

}

