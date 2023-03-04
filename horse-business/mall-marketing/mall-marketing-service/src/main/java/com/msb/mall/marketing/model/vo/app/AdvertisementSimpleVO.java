package com.msb.mall.marketing.model.vo.app;


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
public class AdvertisementSimpleVO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("广告名称")
    private String name;

    @ApiModelProperty("广告平台（1-移动端 2-PC端）")
    private Integer platform;

    @ApiModelProperty("广告位置（1-首页轮播 2-首页金刚区 3-商品分类广告）")
    private Integer location;

    @ApiModelProperty("商品分类id")
    private Long productCategoryId;

    @ApiModelProperty("广告图片地址")
    private String pictureUrl;

    @ApiModelProperty("跳转类型（1-商品详情 2-分类商品列表 3-链接 4-不跳转）")
    private Integer jumpType;

    @ApiModelProperty("跳转地址")
    private String jumpUrl;


}

