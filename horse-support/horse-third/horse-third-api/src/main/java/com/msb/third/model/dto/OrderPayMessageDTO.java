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
@ApiModel("订单支付成功模板DTO")
public class OrderPayMessageDTO extends TemplateMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("实付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("商品名称")
    private String productNames;

    @ApiModelProperty("用户昵称")
    private String userNickName;

    public OrderPayMessageDTO(WxMpAppEnum wxMpAppEnum, Long primaryId, Long userId) {
        super(wxMpAppEnum, primaryId, userId);
    }

}
