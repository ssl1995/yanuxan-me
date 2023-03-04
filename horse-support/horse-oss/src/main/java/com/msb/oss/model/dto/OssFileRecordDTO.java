package com.msb.oss.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;


/**
 * (OssFileRecord)表实体类
 *
 * @author makejava
 * @date 2022-03-30 10:48:39
 */
@Data
public class OssFileRecordDTO implements Serializable {

    private Long id;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("所属服务")
    private String service;

    @ApiModelProperty("存放目录")
    private String directory;

    @ApiModelProperty("文件内容(MD5加密)")
    private String fileMd5;

    @ApiModelProperty("文件地址")
    private String fileUrl;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("是否有用到")
    private Integer used;

    @ApiModelProperty("文件大小")
    private Integer fileSize;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}

