package com.msb.search.model.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * (SystemConfig)表实体类
 *
 * @author luozhan
 * @since 2022-06-10 15:08:10
 */
@Data
public class SystemConfigVO implements Serializable {

    private Long id;

    @ApiModelProperty("系统编码")
    private String systemCode;

    @ApiModelProperty("系统名称")
    private String systemName;

    @ApiModelProperty("数据库类型，1-mysql")
    private Integer databaseType;

    @ApiModelProperty("数据库配置（格式：“ip:port”）")
    private String datasource;

    @ApiModelProperty("逻辑删除")
    private Boolean isDeleted;

}

