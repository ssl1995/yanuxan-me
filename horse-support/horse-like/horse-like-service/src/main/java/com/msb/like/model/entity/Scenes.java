package com.msb.like.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 场景表(Scenes)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-22 20:31:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("scenes")
public class Scenes extends BaseEntity implements Serializable {

    /**
     * 场景id 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
  
    /**
     * 场景标识 
     */
    private String scenesCode;
  
    /**
     * 场景名称 
     */
    private String scenesName;
  
    /**
     * 逻辑删除 
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;
  
 
 
 
 
}

