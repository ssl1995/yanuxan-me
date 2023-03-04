package com.msb.mall.marketing.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * 广告(Advertisement)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
@Data
public class AdvertisementModifyDTO implements Serializable {
    @Size(max = 50)
    @NotBlank
    @ApiModelProperty("广告名称")
    private String name;

    @NotNull
    @ApiModelProperty("广告平台（1-移动端 2-PC端）")
    private Integer platform;

    @NotNull
    @ApiModelProperty("广告位置（1-首页轮播 2-首页金刚区 3-商品分类广告）")
    private Integer location;
    
    @ApiModelProperty("商品分类id")
    private Long productCategoryId;

    @Size(max = 255)
    @NotBlank
    @ApiModelProperty("广告图片地址")
    private String pictureUrl;
    
    @ApiModelProperty("广告开始时间")
    private LocalDateTime advertisementStartTime;
    
    @ApiModelProperty("广告结束时间")
    private LocalDateTime advertisementEndTime;

    @NotNull
    @ApiModelProperty("跳转类型（1-商品详情 2-分类商品列表 3-链接 4-不跳转）")
    private Integer jumpType;

    @Size(max = 255)
    @ApiModelProperty("跳转地址")
    private String jumpUrl;
    
    @ApiModelProperty("启用状态（0-未启用 1-已启用）")
    private Boolean isEnable;
    
}

