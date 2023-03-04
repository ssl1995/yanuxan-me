package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.transform.annotation.Transform;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("后管退款单详情（仅退款）VO")
public class RefundInfoByOnlyRefundVO extends RefundInfoAdminVO {

    @Transform
    @ApiModelProperty("同意退款/拒绝退款-操作日志")
    private RefundLogInfoVO handleRefundLog;

    public RefundInfoByOnlyRefundVO() {
        super();
    }

}
