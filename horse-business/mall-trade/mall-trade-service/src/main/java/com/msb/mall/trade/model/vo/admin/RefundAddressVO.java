package com.msb.mall.trade.model.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("退货地址收货点VO")
public class RefundAddressVO {

    @ApiModelProperty(value = "收件人姓名", required = true)
    private String recipientName;

    @ApiModelProperty(value = "收件人号码", required = true)
    private String recipientPhone;

    @ApiModelProperty(value = "省区域代码", required = true)
    private String provinceCode;

    @ApiModelProperty(value = "省", required = true)
    private String province;

    @ApiModelProperty(value = "市区域代码", required = true)
    private String cityCode;

    @ApiModelProperty(value = "市", required = true)
    private String city;

    @ApiModelProperty(value = "区/县代码", required = true)
    private String areaCode;

    @ApiModelProperty(value = "区/县", required = true)
    private String area;

    @ApiModelProperty(value = "详细地址", required = true)
    private String detailAddress;

}
