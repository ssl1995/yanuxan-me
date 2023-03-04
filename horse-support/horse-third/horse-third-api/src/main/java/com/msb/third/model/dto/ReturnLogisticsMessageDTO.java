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
@ApiModel("填写退货物流模板DTO")
public class ReturnLogisticsMessageDTO extends TemplateMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退款单编号")
    private String refundNo;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("商品数量")
    private Integer quantity;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    public ReturnLogisticsMessageDTO(WxMpAppEnum wxMpAppEnum, Long primaryId, Long userId) {
        super(wxMpAppEnum, primaryId, userId);
    }

}
