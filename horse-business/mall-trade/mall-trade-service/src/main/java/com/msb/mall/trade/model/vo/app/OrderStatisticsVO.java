package com.msb.mall.trade.model.vo.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("订单统计数据VO")
public class OrderStatisticsVO {

    @ApiModelProperty("全部统计数量")
    private Integer allCount;

    @ApiModelProperty("待付款统计数量")
    private Integer unpaidCount;

    @ApiModelProperty("已关闭统计数量")
    private Integer closeCount;

    @ApiModelProperty("待发货统计数量")
    private Integer waitDeliveryCount;

    @ApiModelProperty("已发货统计数量")
    private Integer deliveredCount;

    @ApiModelProperty("已收货统计数量")
    private Integer receivingCount;

    @ApiModelProperty("已完成统计数量")
    private Integer finishCount;

    @ApiModelProperty("待评价数量")
    private Integer waitComment;

}
