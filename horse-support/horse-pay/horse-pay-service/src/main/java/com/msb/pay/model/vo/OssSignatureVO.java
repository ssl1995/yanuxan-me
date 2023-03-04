package com.msb.pay.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("OSS调用签名VO")
public class OssSignatureVO {

    @ApiModelProperty("accessId")
    private String accessId;

    @ApiModelProperty("policy")
    private String policy;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("目录")
    private String dir;

    @ApiModelProperty("请求节点")
    private String host;

    @ApiModelProperty("过期时间")
    private Long expire;

    @ApiModelProperty("回调信息")
    private String callback;

    @ApiModelProperty("MD5")
    private List<String> md5;

}
