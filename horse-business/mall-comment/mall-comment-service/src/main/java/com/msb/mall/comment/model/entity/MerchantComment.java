package com.msb.mall.comment.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;



/**
 * 商家评论(MerchantComment)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-20 16:14:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("merchant_comment")
public class MerchantComment extends BaseEntity implements Serializable {

    /**
     * id 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
  
    /**
     * 来源id 
     */
    private Long originId;
  
    /**
     * 用户id 
     */
    private Long userId;
  
    /**
     * 评论内容 
     */
    private String commentContent;
  
    /**
     * 是否显示（0-否 1-是） 
     */
    private Boolean isShow;
  
    /**
     * 是否删除（0-否 1-是） 
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean isDeleted;
}

