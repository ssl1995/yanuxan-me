package com.msb.im.model.dto;



import com.msb.framework.common.model.PageDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;


/**
 * (HorseImThirdSys)表实体类
 *
 * @author zhoumiao
 * @since 2022-04-25 16:24:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统DTO")
public class ThirdSystemConfigDTO extends PageDTO implements Serializable {

        @ApiModelProperty("系统名称")
        private String name;

}

