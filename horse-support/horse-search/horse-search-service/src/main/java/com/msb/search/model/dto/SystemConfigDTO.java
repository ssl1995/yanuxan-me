package com.msb.search.model.dto;


import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;


/**
 * (SystemConfig)表实体类
 *
 * @author luozhan
 * @since 2022-06-10 15:08:10
 */
@Data
public class SystemConfigDTO extends PageDTO {

    private Long id;

    @NotNull
    @Length(max = 20)
    @ApiModelProperty("系统编码")
    private String systemCode;
    @NotNull
    @Length(max = 50)
    @ApiModelProperty("系统名称")
    private String systemName;

    @ApiModelProperty("数据库类型，1-mysql")
    private Integer databaseType;

    @ApiModelProperty("数据库配置（格式：“ip:port”）")
    private String datasource;

    @ApiModelProperty("逻辑删除")
    private Boolean isDeleted;

}

