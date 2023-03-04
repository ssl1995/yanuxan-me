package com.msb.search.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;


/**
 * (SystemConfig)表实体类
 *
 * @author luozhan
 * @since 2022-06-10 15:08:09
 */
@Data
@TableName("system_config")
public class SystemConfig implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 系统编码
     */
    private String systemCode;

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 数据库类型，1-mysql
     */
    private Integer databaseType;

    /**
     * 数据库配置（格式：“ip:port”）
     */
    private String datasource;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}

