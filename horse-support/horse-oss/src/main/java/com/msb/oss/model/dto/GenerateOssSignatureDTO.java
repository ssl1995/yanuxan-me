package com.msb.oss.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Map;

@Data
public class GenerateOssSignatureDTO {

    @ApiModelProperty("服务名")
    private String serviceName;
    @ApiModelProperty("业务配置id")
    private String configId;
    @ApiModelProperty("业务配置参数")
    private Map<String, String> paramMap;
}
