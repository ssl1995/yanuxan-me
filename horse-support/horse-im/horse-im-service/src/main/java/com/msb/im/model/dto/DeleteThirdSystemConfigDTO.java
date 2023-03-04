package com.msb.im.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


/**
 * (HorseImThirdSys)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Data
@ApiModel("删除系统DTO")
public class DeleteThirdSystemConfigDTO implements Serializable {

        @ApiModelProperty("系统id")
        @NotEmpty(message = "id不能为空")
        private List<Integer> ids;

}

