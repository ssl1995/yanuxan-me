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
@ApiModel("APP退款单物流VO")
public class RefundLogisticsVO {

    @ApiModelProperty("退款单ID")
    private Long refundId;

    @ApiModelProperty("收件人姓名")
    private String recipientName;

    @ApiModelProperty("收件人号码")
    private String recipientPhone;

    @ApiModelProperty("完整收货地址")
    private String recipientAddress;

    @ApiModelProperty("物流公司编号")
    private String companyCode;

    @ApiModelProperty("物流公司名称")
    private String companyName;

    @ApiModelProperty("物流单号")
    private String trackingNo;

    @ApiModelPropertyEnum(dictEnum = LogisticsStatusEnum.class)
    @ApiModelProperty("物流状态")
    private Integer logisticsStatus;

    @TransformEnum(value = LogisticsStatusEnum.class, from = "logisticsStatus")
    @ApiModelProperty("物流状态文本")
    private String logisticsStatusDesc;

    @ApiModelProperty("物流详情数据")
    private List<LogisticsDataVO> logisticsDataList;

    public RefundLogisticsVO() {
        this.logisticsDataList = Collections.emptyList();
    }

}
