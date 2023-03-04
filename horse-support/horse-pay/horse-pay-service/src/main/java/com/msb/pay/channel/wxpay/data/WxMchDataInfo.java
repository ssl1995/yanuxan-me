package com.msb.pay.channel.wxpay.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("微信商户资料信息")
public class WxMchDataInfo {

    @ApiModelProperty(value = "API密钥")
    private String apiKey;

    @ApiModelProperty(value = "v3密钥")
    private String apiKeyV3;

    @ApiModelProperty(value = "证书序列号")
    private String serialNo;


    @ApiModelProperty(value = "私钥证书 apiclient_key.pem OSS下载路径")
    private String keyOssUrl;

    @ApiModelProperty(value = "私钥证书 apiclient_key.pem 证书本地路径")
    private String keyPath;


    @ApiModelProperty(value = "商户证书 apiclient_cert.pem OSS下载路径")
    private String certOssUrl;

    @ApiModelProperty(value = "商户证书 apiclient_cert.pem 证书本地路径")
    private String certPath;


    @ApiModelProperty(value = "p12证书 apiclient_cert.p12 OSS下载路径")
    private String certP12OssUrl;

    @ApiModelProperty(value = "p12证书 apiclient_cert.p12 证书本地路径")
    private String certP12Path;


    @ApiModelProperty(value = "平台证书 platform_cert.pem OSS下载路径")
    private String platformCertOssUrl;

    @ApiModelProperty(value = "平台证书 platform_cert.pem 证书本地路径")
    private String platformCertPath;

}
