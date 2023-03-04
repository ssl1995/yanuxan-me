package com.msb.pay.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

@Data
@ApiModel("签名对象")
public abstract class BaseSign implements Serializable {

    @NotBlank
    @ApiModelProperty(value = "签名", required = true)
    private String sign;

    @NotNull
    @ApiModelProperty(value = "时间戳", required = true)
    private Long timestamp;

    @ApiModelProperty(value = "扩展数据", required = false)
    private Map<String, Object> extParam;

}
