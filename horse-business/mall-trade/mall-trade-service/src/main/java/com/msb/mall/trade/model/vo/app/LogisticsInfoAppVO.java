package com.msb.mall.trade.model.vo.app;

import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.framework.web.transform.annotation.Transform;
import com.msb.framework.web.transform.annotation.TransformEnum;
import com.msb.mall.trade.enums.LogisticsStatusEnum;
import com.msb.mall.trade.enums.OrderTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel("APP物流信息VO")
public class LogisticsInfoAppVO {

    public LogisticsInfoAppVO() {
        this.logisticsDataList = Collections.emptyList();
    }

    @ApiModelProperty("订单ID")
    private Long orderId;

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

    @Transform
    @ApiModelProperty("订单商品详情")
    private List<OrderProductVO> products;

    @ApiModelProperty("物流详情数据")
    private List<LogisticsDataVO> logisticsDataList;

    @ApiModelProperty("订单类型")
    @ApiModelPropertyEnum(dictEnum = OrderTypeEnum.class)
    private Integer orderType;

}
