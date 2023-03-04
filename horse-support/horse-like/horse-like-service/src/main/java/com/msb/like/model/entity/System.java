package com.msb.like.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 系统表(System)表实体类
 *
 * @author shumengjiao
 * @since 2022-06-22 20:31:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system")
public class System extends BaseEntity implements Serializable {

    /**
     * 系统id 
     */
    @TableId(type = IdType.AUTO)
    private Long id;
  
    /**
     * 系统标识 
     */
    private String systemCode;
  
    /**
     * 系统名称 
     */
    private String systemName;
  
    /**
     * 逻辑删除 
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer isDeleted;
  
 
 
 
 
}

