package com.msb.mall.marketing.model.vo;



import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;


/**
 * 广告(Advertisement)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
@Data
public class AdvertisementVO implements Serializable {

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
    
    @ApiModelProperty("广告开始时间")
        private LocalDateTime advertisementStartTime;
    
    @ApiModelProperty("广告结束时间")
        private LocalDateTime advertisementEndTime;
    
    @ApiModelProperty("跳转类型（1-商品详情 2-分类商品列表 3-链接 4-不跳转）")
        private Integer jumpType;
    
    @ApiModelProperty("跳转地址")
        private String jumpUrl;
    
    @ApiModelProperty("启用状态（0-未启用 1-已启用）")
        private Boolean isEnable;
    
    @ApiModelProperty("排序")
        private Integer sort;
    
    @ApiModelProperty("删除状态（0-未删除 1-已删除）")
        private Boolean isDeleted;
    
    @ApiModelProperty("创建时间")
        private LocalDateTime createTime;
    
    @ApiModelProperty("创建人")
        private Long createUser;
    
    @ApiModelProperty("更新时间")
        private LocalDateTime updateTime;
    
    @ApiModelProperty("更新人")
        private Long updateUser;
    
}

