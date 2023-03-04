package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.LogisticsStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel("订单物流信息VO")
public class OrderLogisticsVO {

    @ApiModelProperty("收件人姓名")
    private String recipientName;

    @ApiModelProperty("收件人号码")
    private String recipientPhone;

    @ApiModelProperty("完整收货地址")
    private String recipientAddress;

    @ApiModelProperty("省区域代码")
    private String provinceCode;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市区域代码")
    private String cityCode;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区/县代码")
    private String areaCode;

    @ApiModelProperty("区/县")
    private String area;

    @ApiModelProperty("详细地址")
    private String detailAddress;

    @ApiModelPropertyEnum(dictEnum = LogisticsStatusEnum.class)
    @ApiModelProperty("物流状态")
    private Integer logisticsStatus;

    @TransformEnum(value = LogisticsStatusEnum.class, from = "logisticsStatus")
    @ApiModelProperty("物流状态文本")
    private String logisticsStatusDesc;

    @ApiModelProperty("物流公司编号")
    private String companyCode;

    @ApiModelProperty("物流公司名称")
    private String companyName;

    @ApiModelProperty("物流单号")
    private String trackingNo;

    @ApiModelProperty("物流详情数据")
    private List<LogisticsDataVO> logisticsDataList;

    public OrderLogisticsVO() {
        this.logisticsDataList = Collections.emptyList();
    }

}
