package com.msb.mall.marketing.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;



/**
 * 广告(Advertisement)表实体类
 *
 * @author shumengjiao
 * @since 2022-05-21 16:36:27
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("advertisement")
public class Advertisement extends BaseEntity implements Serializable {

    /**
     * id 
     */
     @TableId(type = IdType.AUTO)
     private Long id;
        
    /**
     * 广告名称 
     */
    private String name;
        
    /**
     * 广告平台（1-移动端 2-PC端） 
     */
    private Integer platform;
        
    /**
     * 广告位置（1-首页轮播 2-首页金刚区 3-商品分类广告） 
     */
    private Integer location;
        
    /**
     * 商品分类id 
     */
    private Long productCategoryId;
        
    /**
     * 广告图片地址 
     */
    private String pictureUrl;
        
    /**
     * 广告开始时间 
     */
    private LocalDateTime advertisementStartTime;
        
    /**
     * 广告结束时间 
     */
    private LocalDateTime advertisementEndTime;
        
    /**
     * 跳转类型（1-商品详情 2-分类商品列表 3-链接 4-不跳转） 
     */
    private Integer jumpType;
        
    /**
     * 跳转地址 
     */
    private String jumpUrl;
        
    /**
     * 启用状态（0-未启用 1-已启用） 
     */
    private Boolean isEnable;
        
    /**
     * 排序 
     */
     private Integer sort;
        
    /**
     * 删除状态（0-未删除 1-已删除） 
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;
                    
}

