package com.msb.oss.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.msb.framework.mysql.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * (OssFileRecord)表实体类
 *
 * @author makejava
 * @date 2022-03-30 10:48:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("oss_file_record")
public class OssFileRecord extends BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 所属服务
     */
    private String service;

    /**
     * 存放目录
     */
    private String directory;

    /**
     * 文件内容(MD5加密)
     */
    private String fileMd5;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 是否有用到
     */
    private Integer used;

    /**
     * 文件大小
     */
    private Integer fileSize;


    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

}
