package com.msb.like.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 点赞表(Like)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-22 21:12:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("likes")
public class Likes extends BaseEntity implements Serializable {

    /**
     * 点赞id 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
  
    /**
     * 系统id 
     */
    private Long systemId;
  
    /**
     * 场景id 
     */
    private Long scenesId;
  
    /**
     * 业务id 
     */
    private Long businessId;
  
    /**
     * 用户id 
     */
    private Long userId;
  
}

