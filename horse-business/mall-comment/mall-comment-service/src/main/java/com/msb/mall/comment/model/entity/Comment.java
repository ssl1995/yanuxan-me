package com.msb.mall.comment.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;



/**
 * (Comment)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-13 21:24:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
@Accessors(chain = true)
public class Comment extends BaseEntity implements Serializable {

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
     * 父id 
     */
    private Long parentId;
  
    /**
     * 用户id 
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;
  
    /**
     * 订单商品id
     */
    private Long orderProductId;
  
    /**
     * 评论内容 
     */
    private String commentContent;
  
    /**
     * 图片地址 
     */
    private String pictureUrl;

    /**
     * 评论标签 1-晒图 2-追评
     */
    private String label;
  
    /**
     * 有用数 
     */
    private Integer usefulCount;
  
    /**
     * 评论类型（1-评论 2-追评 3-回复） 
     */
    private Integer commentType;
  
    /**
     * 是否显示（0-否 1-是）
     */
    private Boolean isShow;
  
    /**
     * 是否默认评价（0-否 1-是） 
     */
    private Boolean isDefaultComment;
  
    /**
     * 是否删除（0-否 1-是） 
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Boolean isDeleted;
 
    /**
     * 评价分值（1-5星） 
     */
    private Integer commentScore;
  
}

