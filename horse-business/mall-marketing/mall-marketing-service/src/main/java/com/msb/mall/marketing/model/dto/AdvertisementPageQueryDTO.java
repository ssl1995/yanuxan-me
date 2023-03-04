package com.msb.mall.marketing.model.dto;

import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 广告(Advertisement)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
@Data
public class AdvertisementPageQueryDTO extends PageDTO implements Serializable  {

    @ApiModelProperty("广告名称")
    private String name;

    @ApiModelProperty("广告平台（1-移动端 2-PC端）")
    private Integer platform;

    @ApiModelProperty("广告位置（1-首页轮播 2-首页金刚区 3-商品分类广告）")
    private Integer location;

    @ApiModelProperty("广告结束时间-开始区间")
    private LocalDateTime advertisementEndTime;

    @ApiModelProperty("广告结束时间-结束区间")
    private LocalDateTime advertisementEndTimeEnd;

    @ApiModelProperty("启用状态（0-未启用 1-已启用）")
    private Boolean isEnable;


}
