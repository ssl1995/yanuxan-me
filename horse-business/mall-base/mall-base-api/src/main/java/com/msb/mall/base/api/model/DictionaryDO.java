package com.msb.mall.base.api.model;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 数据字典表（任何有可能修改的以及有业务含义的数据字典或者前端需要用来展示下拉框）(Dictionary)表实体类
 *
 * @author makejava
 * @date 2022-03-31 15:04:14
 */
@Data
public class DictionaryDO implements Serializable {

    private Integer id;

    @ApiModelProperty("字典分类，使用大写驼峰命名")
    private String type;

    @ApiModelProperty("描述")
    private String desc;

    @ApiModelProperty("编码值，编码值应无任何业务意义，使用1，2，3...设值")
    private Integer code;

    @ApiModelProperty("文本")
    private String text;

    @ApiModelProperty("状态（0-停用，1-开启）")
    private Boolean isEnable;

    private LocalDateTime createTime;

    @ApiModelProperty("填写创建人的名字")
    private String createUser;

    private LocalDateTime updateTime;

    private String updateUser;
}

