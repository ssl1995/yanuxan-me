package com.msb.mall.trade.model.vo.admin;

import com.msb.framework.web.transform.annotation.Transform;
import com.msb.mall.trade.model.vo.app.RefundEvidenceVO;
import com.msb.mall.trade.model.vo.app.RefundLogisticsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel("后管退款单详情（退货退款）VO")
public class RefundInfoByReturnVO extends RefundInfoAdminVO {

    @ApiModelProperty("退货单收货点信息")
    private RefundAddressVO refundAddress;

    @Transform
    @ApiModelProperty("退款单物流信息")
    private RefundLogisticsVO refundLogistics;

    @ApiModelProperty("填写退货物流备注")
    private String refundLogisticsRemark;

    @Transform
    @ApiModelProperty("退货物流凭证信息")
    private List<RefundEvidenceVO> logisticsEvidences;

    @Transform
    @ApiModelProperty("同意退货/拒绝退货-操作日志")
    private RefundLogInfoVO handleReturnLog;

    @Transform
    @ApiModelProperty("确认收货/拒绝收货-操作日志")
    private RefundLogInfoVO handleReceivingLog;

    public RefundInfoByReturnVO() {
        super();
        logisticsEvidences = Collections.emptyList();
    }

}
