package com.msb.mall.trade.model.vo.admin;

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
@ApiModel("后管退款单统计数据VO")
public class RefundStatisticsAdminVO {

    @ApiModelProperty("全部统计数量")
    private Integer allCount;

    @ApiModelProperty("已申请统计数量")
    private Integer applyCount;

    @ApiModelProperty("已关闭统计数量")
    private Integer closeCount;

    @ApiModelProperty("待退货统计数量")
    private Integer waitReturnCount;

    @ApiModelProperty("退货中统计数量")
    private Integer inReturnCount;

    @ApiModelProperty("退款中统计数量")
    private Integer inRefundCount;

    @ApiModelProperty("退款成功统计数量")
    private Integer refundSuccessCount;

    @ApiModelProperty("退款失败统计数量")
    private Integer refundFailCount;

}
