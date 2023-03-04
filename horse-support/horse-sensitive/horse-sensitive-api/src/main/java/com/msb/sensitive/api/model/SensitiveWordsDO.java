package com.msb.sensitive.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 86151
 */
@Data
@Accessors(chain = true)
public class SensitiveWordsDO implements Serializable {
    @ApiModelProperty("是否包含敏感词")
    private Boolean isContainSensitiveWords;
    @ApiModelProperty("替换敏感词后的内容")
    private String content;
}
