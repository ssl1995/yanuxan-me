package com.msb.third.model.dto;

import com.msb.third.enums.WxMpAppEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("退款失败模板DTO")
public class RefundFailMessageDTO extends TemplateMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退款单编号")
    private String refundNo;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("关闭原因")
    private String closeReason;

    public RefundFailMessageDTO(WxMpAppEnum wxMpAppEnum, Long primaryId, Long userId) {
        super(wxMpAppEnum, primaryId, userId);
    }

}
