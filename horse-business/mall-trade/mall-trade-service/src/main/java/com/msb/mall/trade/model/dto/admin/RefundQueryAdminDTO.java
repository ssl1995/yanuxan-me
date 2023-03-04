package com.msb.mall.trade.model.dto.admin;

import com.msb.framework.common.model.PageDTO;
import com.msb.framework.web.swagger.ApiModelPropertyEnum;
import com.msb.mall.trade.enums.RefundHandleEnum;
import com.msb.mall.trade.enums.RefundStatusEnum;
import com.msb.mall.trade.enums.RefundTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel("后管退款单列表DTO")
public class RefundQueryAdminDTO extends PageDTO {

    @Length(max = 32)
    @ApiModelProperty(value = "退款单号", required = false)
    private String refundNo;

    @Length(max = 11)
    @ApiModelProperty(value = "用户号码", required = false)
    private String userPhone;

    @Min(1)
    @Max(2)
    @ApiModelPropertyEnum(dictEnum = RefundTypeEnum.class)
    @ApiModelProperty(value = "退款单类型", required = false)
    private Integer refundType;

    @ApiModelPropertyEnum(dictEnum = RefundStatusEnum.class)
    @ApiModelProperty(value = "退款单状态，示例：1,2,3", required = false)
    private List<Integer> refundStatus;

    @Min(1)
    @Max(7)
    @ApiModelPropertyEnum(dictEnum = RefundHandleEnum.class)
    @ApiModelProperty(value = "商家处理状态", required = false)
    private Integer handleStatus;

    @ApiModelProperty(value = "申请开始时间（yyyy-MM-dd HH:mm:ss）", required = false)
    private LocalDateTime applyStartTime;

    @ApiModelProperty(value = "申请结束时间（yyyy-MM-dd HH:mm:ss）", required = false)
    private LocalDateTime applyEndTime;

    public RefundQueryAdminDTO() {
        this.refundStatus = Collections.emptyList();
    }

}
