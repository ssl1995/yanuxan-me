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
 * 系统客户端表(SystemClient)表实体类
 *
 * @author makejava
 * @date 2022-04-25 16:56:40
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("system_client")
public class SystemClient extends BaseEntity implements Serializable {

    /**
     * 客户端id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统id
     */
    private Long systemId;

    /**
     * 客户端标识
     */
    private String clientCode;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 0 不可用  1可用
     */
    private Boolean isEnable;


    /**
     * 逻辑删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
