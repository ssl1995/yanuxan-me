package com.msb.mall.trade.model.vo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel("订单物流数据VO")
public class LogisticsDataVO {

    @ApiModelProperty("物流时间")
    private String time;

    @ApiModelProperty("数据内容")
    private String context;

}
