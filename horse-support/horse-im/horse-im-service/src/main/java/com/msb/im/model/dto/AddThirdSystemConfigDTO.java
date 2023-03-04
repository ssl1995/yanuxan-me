package com.msb.im.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * (HorseImThirdSys)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Data
@ApiModel("新增系统DTO")
public class AddThirdSystemConfigDTO implements Serializable {

        @ApiModelProperty(value = "系统名称",required = true)
        @NotBlank(message = "名称不能为空")
        private String name;

        @ApiModelProperty(value = "系统标识",required = true)
        @NotBlank(message = "系统标识不能为空")
        private String client;

}

