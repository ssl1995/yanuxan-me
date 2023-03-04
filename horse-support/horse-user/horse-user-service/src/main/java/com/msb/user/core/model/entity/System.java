package com.msb.user.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.experimental.Accessors;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 系统表(System)表实体类
 *
 * @author makejava
 * @date 2022-04-25 16:56:41
 */
@Accessors(chain = true)
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
    private Boolean isDeleted;

}
