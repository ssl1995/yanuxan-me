package com.msb.third.model.dto;

import com.msb.third.enums.WxMpAppEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("订单发货模板DTO")
public class OrderDeliveryMessageDTO extends TemplateMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("物流公司名称")
    private String companyName;

    @ApiModelProperty("物流单号")
    private String trackingNo;

    public OrderDeliveryMessageDTO(WxMpAppEnum wxMpAppEnum, Long primaryId, Long userId) {
        super(wxMpAppEnum, primaryId, userId);
    }

}
