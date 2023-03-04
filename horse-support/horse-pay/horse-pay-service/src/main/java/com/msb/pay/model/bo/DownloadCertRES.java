package com.msb.pay.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel("证书下载响应")
public class DownloadCertRES {

    @ApiModelProperty("下载结果")
    private boolean download;

    @ApiModelProperty("OSS桶路径")
    private String ossUrl;

    @ApiModelProperty("本地文件路径")
    private String filePath;

}
